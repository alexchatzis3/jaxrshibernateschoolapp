package gr.aueb.cf.schoolapp.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utility class providing validation support for DTO objects using the
 * Jakarta Bean Validation API.
 *
 * <p>This class initializes a single {@link Validator} instance for the application's
 * lifecycle. Consumers can call {@link #validateDTO(Object)} to validate DTOs and
 * collect constraint violation messages.</p>
 *
 * <p>Designed to be stateless and not instantiable.</p>
 */
public class ValidatorUtil {

    /** Shared Validator instance used for all validation operations. */
    private static final Validator validator;

    /** Logger instance for reporting validation-related initialization errors. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorUtil.class);

    // Static initialization block for validator initialization
    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            LOGGER.error("Error. Validator can not be initialized.");
            throw e;
        }
    }

    /**
     * Private constructor to prevent instantiation since this class is a utility class.
     */
    private ValidatorUtil() {}

    /**
     * Validates a DTO object using Jakarta Bean Validation and returns validation error messages.
     *
     * <p>If no constraints are violated, an empty list is returned.</p>
     *
     * @param dto the DTO instance to validate
     * @param <T> the DTO type parameter
     * @return a list of human-readable error messages, possibly empty
     */
    public static <T> List<String> validateDTO(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        List<String> errors = new ArrayList<>();

        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> violation : violations) {
                errors.add(violation.getMessage());
            }
        }
        return errors;
    }
}
