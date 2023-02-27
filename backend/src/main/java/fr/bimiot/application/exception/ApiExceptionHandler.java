package fr.bimiot.application.exception;

import fr.bimiot.application.exception.type.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<Object> handleException(BaseException baseException) {
        return switch (baseException.getClass().getSimpleName()) {
            case "DomainException", "DatabaseException" -> handleRecoverableException(baseException);
            default -> handleGenericException(baseException);
        };
    }

    private ResponseEntity<Object> handleGenericException(BaseException exception) {
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("code", "200");
        errorDetails.put("type", errorDetails.getClass().getSimpleName());
        errorDetails.put("message", exception.getMessage());
        errorDetails.put("trace", exception.getStackTrace());
        return new ResponseEntity<>(errorDetails, requireNonNull(HttpStatus.resolve(200)));
    }

    private ResponseEntity<Object> handleRecoverableException(BaseException baseException) {
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("code", baseException.getCode());
        errorDetails.put("type", baseException.getClass().getSimpleName());
        errorDetails.put("message", baseException.getMessage());
        errorDetails.put("trace", baseException.getStackTrace());
        return new ResponseEntity<>(errorDetails, requireNonNull(HttpStatus.resolve(Integer.parseInt(baseException.getCode()))));
    }
}
