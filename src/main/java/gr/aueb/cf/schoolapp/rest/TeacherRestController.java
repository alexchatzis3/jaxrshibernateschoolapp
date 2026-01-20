package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.TeacherFiltersDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing {@code Teacher} resources.
 *
 * <p>Exposes CRUD and filtering operations for teachers via HTTP endpoints.
 * Performs validation using {@link ValidatorUtil} and delegates business logic
 * to {@link ITeacherService}.</p>
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path("/teachers")
public class TeacherRestController {

    /** Service layer dependency for teacher operations */
    private final ITeacherService teacherService;

    /**
     * Creates a new teacher.
     *
     * @param insertDTO teacher data to insert
     * @param uriInfo   URI context for building resource location
     * @return HTTP 201 Created with the created teacher DTO
     * @throws EntityInvalidArgumentException if validation fails
     * @throws EntityAlreadyExistsException   if a teacher with the same VAT already exists
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTeacher(TeacherInsertDTO insertDTO, @Context UriInfo uriInfo)
            throws EntityInvalidArgumentException, EntityAlreadyExistsException {

        // Validate DTO fields
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Teacher", String.join(", ", errors));
        }

        // Delegate insertion to service layer
        TeacherReadOnlyDTO readOnlyDTO = teacherService.insertTeacher(insertDTO);

        // Return 201 Created with location header
        return Response.created(
                        uriInfo.getAbsolutePathBuilder()
                                .path(readOnlyDTO.getId().toString())
                                .build())
                .entity(readOnlyDTO)
                .build();
    }

    /**
     * Updates an existing teacher.
     *
     * @param teacherId ID of the teacher to update
     * @param updateDTO updated teacher data
     * @return HTTP 200 OK with updated teacher DTO
     * @throws EntityInvalidArgumentException if validation fails
     * @throws EntityNotFoundException        if the teacher does not exist
     */
    @PUT
    @Path("/{teacherId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTeacher(@PathParam("teacherId") Long teacherId, TeacherUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException {

        // Validate DTO fields
        List<String> errors = ValidatorUtil.validateDTO(updateDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Teacher", String.join(", ", errors));
        }

        // Delegate update to service layer
        TeacherReadOnlyDTO readOnlyDTO = teacherService.updateTeacher(updateDTO);

        return Response.ok(readOnlyDTO).build();
    }

    /**
     * Deletes a teacher by ID.
     *
     * @param teacherId ID of the teacher to delete
     * @return HTTP 200 OK with the deleted teacher DTO
     * @throws EntityNotFoundException if the teacher does not exist
     */
    @DELETE
    @Path("/{teacherId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTeacher(@PathParam("teacherId") Long teacherId)
            throws EntityNotFoundException {

        // Retrieve teacher before deletion for response
        TeacherReadOnlyDTO readOnlyDTO = teacherService.getTeacherById(teacherId);

        // Delegate deletion to service layer
        teacherService.deleteTeacher(teacherId);

        return Response.ok(readOnlyDTO).build();
    }

    /**
     * Retrieves a teacher by ID.
     *
     * @param id teacher ID
     * @return HTTP 200 OK with teacher DTO
     * @throws EntityNotFoundException if the teacher does not exist
     */
    @GET
    @Path("/{teacherId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeacher(@PathParam("teacherId") Long id)
            throws EntityNotFoundException {

        TeacherReadOnlyDTO readOnlyDTO = teacherService.getTeacherById(id);
        return Response.ok(readOnlyDTO).build();
    }

    /**
     * Retrieves teachers filtered by optional query parameters.
     *
     * @param firstname optional first name filter
     * @param lastname  optional last name filter
     * @param vat       optional VAT filter
     * @return HTTP 200 OK with a list of filtered teacher DTOs
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFiltered(@QueryParam("firstname") String firstname,
                                @QueryParam("lastname") String lastname,
                                @QueryParam("vat") String vat) {

        // Build filter DTO from query parameters
        TeacherFiltersDTO filtersDTO = new TeacherFiltersDTO(firstname, lastname, vat);

        // Convert filter DTO to criteria map
        Map<String, Object> criteria = Mapper.mapToCriteria(filtersDTO);

        // Delegate search to service layer
        List<TeacherReadOnlyDTO> readOnlyDTOS = teacherService.getTeachersByCriteria(criteria);

        return Response.ok(readOnlyDTOS).build();
    }
}
