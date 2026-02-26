package cci.confferenct.conference.exceptions;

import java.util.Map;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String message;
    private Map<String, String> details;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
