package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.authentication.AuthenticationProvider;
import gr.aueb.cf.schoolapp.authentication.AuthenticationResponseDTO;
import gr.aueb.cf.schoolapp.core.exceptions.AppServerException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.UserInsertDTO;
import gr.aueb.cf.schoolapp.dto.UserLoginDTO;
import gr.aueb.cf.schoolapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.schoolapp.security.JwtService;
import gr.aueb.cf.schoolapp.service.IUserService;
import gr.aueb.cf.schoolapp.validator.UserInputValidator;
import gr.aueb.cf.schoolapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * REST controller for handling user authentication actions.
 *
 * <p>Provides endpoints for:
 * <ul>
 *     <li>User registration</li>
 *     <li>User login and token generation</li>
 * </ul>
 *
 * <p>Authentication is based on user credentials validation and JWT token creation.</p>
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
@Path("/auth")
public class AuthRestController {

    private final IUserService userService;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    /**
     * Registers a new user into the system.
     *
     * <p>Steps performed:
     * <ol>
     *     <li>DTO bean validation (JSR-380)</li>
     *     <li>Custom business validation (password match, unique email, etc.)</li>
     *     <li>Persisting the new user</li>
     * </ol>
     *
     * @param userInsertDTO the user data to be inserted
     * @param uriInfo used to build the "Location" header for the created resource
     * @return a Response with HTTP 201 Created including the created user
     * @throws EntityInvalidArgumentException when validation fails
     * @throws AppServerException when a server-level operation fails
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(UserInsertDTO userInsertDTO, @Context UriInfo uriInfo)
            throws EntityInvalidArgumentException, AppServerException {

        // Bean validation (annotations-based)
        List<String> beanErrors = ValidatorUtil.validateDTO(userInsertDTO);
        if (!beanErrors.isEmpty()) {
            throw new EntityInvalidArgumentException("User", String.join(", ", beanErrors));
        }

        // Custom input validation (business rules)
        Map<String, String> otherErrors = UserInputValidator.validate(userInsertDTO);
        if (!otherErrors.isEmpty()) {
            throw new EntityInvalidArgumentException("User", String.join(", ", otherErrors.toString()));
        }

        // Persist new user
        UserReadOnlyDTO userReadOnlyDTO = userService.insertUser(userInsertDTO);

        // Return HTTP 201 Created with Location header
        return Response.created(
                        uriInfo.getAbsolutePathBuilder()
                                .path(userReadOnlyDTO.getId().toString())
                                .build())
                .entity(userReadOnlyDTO)
                .build();
    }

    /**
     * Authenticates a user and generates a JWT token on success.
     *
     * <p>Behavior:
     * <ul>
     *     <li>Checks credentials</li>
     *     <li>Verifies existing authentication context</li>
     *     <li>Returns JWT token for valid user login</li>
     * </ul>
     *
     * @param loginDTO user credentials containing username and password
     * @return HTTP 200 + token if authenticated, otherwise HTTP 401 Unauthorized
     * @throws EntityNotFoundException if user does not exist
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserLoginDTO loginDTO, @Context Principal principal)
            throws EntityNotFoundException {

        // Validate credentials
        boolean isUserValid = authenticationProvider.authenticate(loginDTO);

        if (!isUserValid) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // If already authenticated through security context
        if (principal != null) {
            String username = principal.getName();
            if (loginDTO.getUsername().equals(username)) {
                return Response.ok("Already authenticated").build();
            }
        }

        // Retrieve user's role for token claim
        UserReadOnlyDTO userReadOnlyDTO = userService.getUserByUsername(loginDTO.getUsername());
        String role = userReadOnlyDTO.getRole();

        // Generate JWT token
        String token = jwtService.generateToken(loginDTO.getUsername(), role);

        // Wrap token in DTO and return
        AuthenticationResponseDTO responseDTO = new AuthenticationResponseDTO(token);

        return Response.ok(responseDTO).build();
    }
}
