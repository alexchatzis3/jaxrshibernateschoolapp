package gr.aueb.cf.schoolapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) used to represent structured responses for exceptions or messages.
 *
 * <p>This class provides a code identifying the error or response type,
 * and a human-readable description of the issue.</p>
 */
@Data
@AllArgsConstructor
public class ResponseMessageDTO {

    /** A machine-readable code representing the error or response type */
    private String code;

    /** A human-readable description of the response or error */
    private String description;

    /**
     * Constructor to create a ResponseMessageDTO with only a code.
     * The description is initialized as an empty string.
     *
     * @param code the code representing the response or error
     */
    public ResponseMessageDTO(String code){
        this.code = code;
        this.description = "";
    }
}
