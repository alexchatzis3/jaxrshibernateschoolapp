package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.StudentFiltersDTO;
import gr.aueb.cf.schoolapp.dto.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.StudentReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.StudentUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.service.IStudentService;
import gr.aueb.cf.schoolapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing {@code Student} resources.
 *
 * <p>Exposes CRUD and filtering operations for students via HTTP endpoints.
 * Performs validation using {@link ValidatorUtil} and delegates business logic
 * to {@link IStudentService}.</p>
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
@Path("/students")
public class StudentRestController {

    /** Service layer dependency for student operations */
    private final IStudentService studentService;

    /**
     * Creates a new student.
     *
     * @param insertDTO student data to insert
     * @param uriInfo   URI context for building resource location
     * @return HTTP 201 Created with the created student DTO
     * @throws EntityInvalidArgumentException if validation fails
     * @throws EntityAlreadyExistsException   if a student with the same email already exists
     * @throws EntityNotFoundException        if any referenced courses are not found
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudent(StudentInsertDTO insertDTO, @Context UriInfo uriInfo)
            throws EntityInvalidArgumentException, EntityAlreadyExistsException, EntityNotFoundException {

        // Validate DTO fields
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Student", String.join(", ", errors));
        }

        // Delegate insertion to service layer
        StudentReadOnlyDTO readOnlyDTO = studentService.insertStudent(insertDTO);

        // Return 201 Created with location header
        return Response.created(
                        uriInfo.getAbsolutePathBuilder()
                                .path(readOnlyDTO.getId().toString())
                                .build())
                .entity(readOnlyDTO)
                .build();
    }

    /**
     * Updates an existing student.
     *
     * @param studentId ID of the student to update
     * @param updateDTO updated student data
     * @return HTTP 200 OK with updated student DTO
     * @throws EntityInvalidArgumentException if validation fails
     * @throws EntityNotFoundException        if the student or any referenced courses do not exist
     */
    @PUT
    @Path("/{studentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(@PathParam("studentId") Long studentId,
                                  StudentUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException {

        // Validate DTO fields
        List<String> errors = ValidatorUtil.validateDTO(updateDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Student", String.join(", ", errors));
        }

        // Set ID from path parameter
        updateDTO.setId(studentId);

        // Delegate update to service layer
        StudentReadOnlyDTO readOnlyDTO = studentService.updateStudent(updateDTO);

        return Response.ok(readOnlyDTO).build();
    }

    /**
     * Deletes a student by ID.
     *
     * <p>Access restricted to ADMIN or TEACHER roles.</p>
     *
     * @param id              student ID
     * @param securityContext security context for role checks
     * @return HTTP 200 OK with the deleted student DTO
     * @throws EntityNotFoundException if the student does not exist
     */
    @DELETE
    @Path("/{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStudent(@PathParam("studentId") Long id,
                                  @Context SecurityContext securityContext)
            throws EntityNotFoundException {

        // Role-based access control
        if (!(securityContext.isUserInRole("ADMIN") || securityContext.isUserInRole("TEACHER"))) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // Retrieve student before deletion for response
        StudentReadOnlyDTO readOnlyDTO = studentService.getStudentById(id);

        // Delegate deletion to service layer
        studentService.deleteStudent(id);

        return Response.ok(readOnlyDTO).build();
    }

    /**
     * Retrieves a student by ID.
     *
     * @param id student ID
     * @return HTTP 200 OK with student DTO
     * @throws EntityNotFoundException if the student does not exist
     */
    @GET
    @Path("/{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("studentId") Long id)
            throws EntityNotFoundException {

        StudentReadOnlyDTO readOnlyDTO = studentService.getStudentById(id);
        return Response.ok(readOnlyDTO).build();
    }

    /**
     * Retrieves students filtered by optional query parameters.
     *
     * @param firstname optional first name filter
     * @param lastname  optional last name filter
     * @param email     optional email filter
     * @return HTTP 200 OK with a list of filtered student DTOs
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFiltered(@QueryParam("firstname") String firstname,
                                @QueryParam("lastname") String lastname,
                                @QueryParam("email") String email) {

        // Build filter DTO from query parameters
        StudentFiltersDTO filtersDTO = new StudentFiltersDTO(firstname, lastname, email);

        // Convert filter DTO to criteria map
        Map<String, Object> criteria = Mapper.mapToCriteria(filtersDTO);

        // Delegate search to service layer
        List<StudentReadOnlyDTO> readOnlyDTOS = studentService.getStudentsByCriteria(criteria);

        return Response.ok(readOnlyDTOS).build();
    }
}
