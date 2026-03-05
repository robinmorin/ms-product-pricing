package org.test.between.msproductpricing.application.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final String EMPTY_VALUE = "<empty>";
    private static final String UNKNOWN_VALUE = "<unknown>";
    private static final String MESSAGE = "Message: ";
    private static final String CAUSE = "Cause  : ";

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> processErrorsValidation(ConstraintViolationException constraintViolationException, WebRequest request) {

        Set<String> lstErrors = constraintViolationException.getConstraintViolations()
                                                             .stream()
                                                             .map(ConstraintViolation::getMessage)
                                                             .collect(Collectors.toSet());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, new ArrayList<>(lstErrors), (ServletWebRequest) request);

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> processTypeMismatchError(MethodArgumentTypeMismatchException exception, WebRequest request) {

        var lstErrors = List.of("FieldName: ".concat(Optional.ofNullable(exception.getPropertyName()).orElse(EMPTY_VALUE)),
                                "Message: Field format value is not valid");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, new ArrayList<>(lstErrors), (ServletWebRequest) request);

    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> processHttpClientError(HttpClientErrorException exception, WebRequest request) {

        var lstErrors = List.of(MESSAGE.concat(Optional.of(exception.getStatusText()).orElse(EMPTY_VALUE)),
                                CAUSE.concat(exception.getClass().getSimpleName()));

        return buildErrorResponse(HttpStatus.valueOf(exception.getStatusCode().value()), lstErrors, (ServletWebRequest) request);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> processUnHandleError(Exception exception, WebRequest request) {

        var lstErrors = List.of(MESSAGE.concat(Optional.ofNullable(exception.getMessage()).orElse(EMPTY_VALUE)),
                                CAUSE.concat(Optional.ofNullable(exception.getCause()).map(Throwable::getMessage).orElse(UNKNOWN_VALUE)));

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, lstErrors, (ServletWebRequest) request);

    }

    @ExceptionHandler(value = {RecordNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(RuntimeException exception, WebRequest request) {
        var lstErrors = List.of(MESSAGE.concat(Optional.ofNullable(exception.getMessage()).orElse(EMPTY_VALUE)));
        return buildErrorResponse(HttpStatus.NOT_FOUND, lstErrors, (ServletWebRequest) request);
    }

    private static ResponseEntity<Object> buildErrorResponse(HttpStatus httpStatus, List<String> lstErrors, ServletWebRequest request) {
        HttpServletRequest servletRequest = request.getRequest();
        var errorResponse = Error.builder().timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .status(httpStatus)
                .path(servletRequest.getRequestURI())
                .errors(lstErrors)
                .build();
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Error {
        private final String timestamp;
        private final HttpStatus status;
        private final String path;
        private final List<String> errors;
    }

}
