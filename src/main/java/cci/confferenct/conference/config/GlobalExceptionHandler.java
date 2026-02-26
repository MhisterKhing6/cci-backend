package cci.confferenct.conference.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cci.confferenct.conference.exceptions.EntityNotFound;
import cci.confferenct.conference.exceptions.ExceptionResponse;
import cci.confferenct.conference.exceptions.UserAlreadyExist;
import cci.confferenct.conference.exceptions.WrongCredentials;
import cci.confferenct.conference.exceptions.OperationNotPermitted;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
          .forEach(error ->
              errors.put(error.getField(), error.getDefaultMessage())
          );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExist(UserAlreadyExist ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(WrongCredentials.class)
    public ResponseEntity<ExceptionResponse> handleWrongCredentials(WrongCredentials ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFound ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(OperationNotPermitted.class)
    public ResponseEntity<ExceptionResponse> handleOperationNotpermeinted(OperationNotPermitted ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
}