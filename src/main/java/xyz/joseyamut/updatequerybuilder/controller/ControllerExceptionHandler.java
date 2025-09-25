package xyz.joseyamut.updatequerybuilder.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import xyz.joseyamut.updatequerybuilder.domain.exception.ResourceNotFoundException;
import xyz.joseyamut.updatequerybuilder.util.DateTimeFormatHelper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> handleResourceNotFoundException(ResourceNotFoundException e,
                                                                             HttpServletRequest request) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody();
        createErrorResponseBody(errorResponseBody, HttpStatus.NOT_FOUND,
                e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseBody> handleConstraintViolationException(ConstraintViolationException e,
                                                                                HttpServletRequest request) {
        List<String> combinedErrorMessages = e.getConstraintViolations().stream()
                .filter(Objects::nonNull)
                .map(ConstraintViolation::getMessage)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        ErrorResponseBody errorResponseBody = new ErrorResponseBody();
        createErrorResponseBody(errorResponseBody, HttpStatus.BAD_REQUEST,
                String.valueOf(combinedErrorMessages), request.getRequestURI());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseBody> handleIllegalArgumentException(IllegalArgumentException e,
                                                                            HttpServletRequest request) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody();
        createErrorResponseBody(errorResponseBody, HttpStatus.BAD_REQUEST,
                e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.BAD_REQUEST);
    }


    private void createErrorResponseBody(ErrorResponseBody errorResponseBody, HttpStatus status,
                                                      String errorMessage, String requestUri) {
        errorResponseBody.setHttpStatus(status.value());
        errorResponseBody.setError(status.getReasonPhrase());
        errorResponseBody.setMessage(errorMessage);
        errorResponseBody.setPath(requestUri);
    }

    @Data
    @JsonPropertyOrder({"message", "status", "error", "path", "timestamp"})
    static class ErrorResponseBody {
        @JsonProperty(value = "status")
        private Integer httpStatus;
        private String error;
        private String message;
        private String path;
        private String timestamp = DateTimeFormatHelper.convertWithZonedDateTime(new Timestamp(System.currentTimeMillis()),
                ZoneId.systemDefault().toString(), ZoneId.systemDefault().toString(),
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }
}
