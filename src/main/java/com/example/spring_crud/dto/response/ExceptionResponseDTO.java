package com.example.spring_crud.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "DTO that encapsulates detailed information about errors or exceptions."
)
@JsonPropertyOrder(alphabetic = true)
public record ExceptionResponseDTO(



        @Schema(description = "List of field errors detailing the specific issues with the request.")
        List<FieldError> errors,

        @Schema(description = "HTTP status code associated with the error.", example = "400")
        String status,

        @Schema(description = "A detailed description of the error or exception that occurred.", example = "Missing or invalid required fields.")
        String message

) {
}