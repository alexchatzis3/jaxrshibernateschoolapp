package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.*;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.service.ICourseService;
import gr.aueb.cf.schoolapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing {@code Course} resources.
 *
 * <p>Provides endpoints for CRUD operations and filtering courses.
 * Validates incoming DTOs using {@link ValidatorUtil} and delegates
 * business logic to {@link ICourseService}.</p>
 *
 * <p>Access control is enforced via {@link SecurityContext}.</p>
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path("/courses")
public class CourseRestController {

    /** Service layer dependency for course operations */
    private final ICourseService courseService;

    /**
     * Creates a new course.
     *
     * @param insertDTO course data to insert
     * @param uriInfo   URI context for building resource location
     * @return HTTP 201 Created with created course DTO
     * @throws EntityInvalidArgumentException if validation fails
     * @throws EntityAlreadyExistsException   if a course with the same title exists
     * @throws EntityNotFoundException        if the referenced teacher does not exist
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCourse(CourseInsertDTO insertDTO, @Context UriInfo uriInfo)
            throws EntityInvalidArgumentException, EntityAlreadyExistsException, EntityNotFoundException {

        // Validate DTO fields
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Course", String.join(", ", errors));
        }

        // Delegate insertion to service layer
        CourseReadOnlyDTO readOnlyDTO = courseService.insertCourse(insertDTO);

        // Return 201 Created with location header
        return Response.created(
                        uriInfo.getAbsolutePathBuilder()
                                .path(readOnlyDTO.getId().toString())
                                .build())
                .entity(readOnlyDTO)
                .build();
    }

    /**
     * Updates an existing course.
     *
     * @param courseId  ID of the course to update
     * @param updateDTO updated course data
     * @return HTTP 200 OK with updated course DTO
     * @throws EntityInvalidArgumentException if validation fails
     * @throws EntityNotFoundException        if the course or teacher does not exist
     */
    @PUT
    @Path("/{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourse(@PathParam("courseId") Long courseId,
                                 CourseUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException {

        // Validate DTO fields
        List<String> errors = ValidatorUtil.validateDTO(updateDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Course", String.join(", ", errors));
        }

        // Set ID from path parameter
        updateDTO.setId(courseId);

        // Delegate update to service layer
        CourseReadOnlyDTO readOnlyDTO = courseService.updateCourse(updateDTO);

        return Response.ok(readOnlyDTO).build();
    }

    /**
     * Deletes a course by ID.
     *
     * <p>Access restricted to users with ADMIN role.</p>
     *
     * @param courseId        course ID
     * @param securityContext security context for role checks
     * @return HTTP 200 OK with deleted course DTO
     * @throws EntityNotFoundException if the course does not exist
     */
    @DELETE
    @Path("/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCourse(@PathParam("courseId") Long courseId,
                                 @Context SecurityContext securityContext)
            throws EntityNotFoundException {

        // Role-based access control
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // Retrieve course before deletion for response
        CourseReadOnlyDTO dto = courseService.getCourseById(courseId);

        // Delegate deletion to service layer
        courseService.deleteCourse(courseId);

        return Response.ok(dto).build();
    }

    /**
     * Retrieves a course by ID.
     *
     * @param courseId course ID
     * @return HTTP 200 OK with course DTO
     * @throws EntityNotFoundException if the course does not exist
     */
    @GET
    @Path("/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourse(@PathParam("courseId") Long courseId)
            throws EntityNotFoundException {

        CourseReadOnlyDTO dto = courseService.getCourseById(courseId);
        return Response.ok(dto).build();
    }

    /**
     * Retrieves courses filtered by optional query parameters.
     *
     * @param title     optional title filter
     * @param teacherId optional teacher ID filter
     * @return HTTP 200 OK with a list of filtered course DTOs
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFiltered(@QueryParam("title") String title,
                                @QueryParam("teacherId") Long teacherId) {

        // Build filter DTO from query parameters
        CourseFiltersDTO filtersDTO = new CourseFiltersDTO(title, teacherId);

        // Convert filter DTO to criteria map
        Map<String, Object> criteria = Mapper.mapToCriteria(filtersDTO);

        // Delegate search to service layer
        List<CourseReadOnlyDTO> readOnlyDTOS = courseService.getCoursesByCriteria(criteria);

        return Response.ok(readOnlyDTOS).build();
    }
}
