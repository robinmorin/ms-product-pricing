package org.test.between.msproductpricing.application.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Rest Exception Handler Unit Tests")
class RestExceptionHandlerTest {

    private RestExceptionHandler restExceptionHandler;

    @Mock
    private ServletWebRequest webRequest;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        restExceptionHandler = new RestExceptionHandler();
    }

    @Test
    @DisplayName("Should return 400 Bad Request when ConstraintViolationException occurs")
    void whenConstraintViolationException_thenReturnBadRequest() {
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Invalid field");
        when(exception.getConstraintViolations()).thenReturn(Set.of(violation));
        when(webRequest.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getRequestURI()).thenReturn("/test-path");

        ResponseEntity<Object> response = restExceptionHandler.processErrorsValidation(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        RestExceptionHandler.Error error = (RestExceptionHandler.Error) response.getBody();
        assertNotNull(error);
        assertEquals("/test-path", error.getPath());
        assertEquals(List.of("Invalid field"), error.getErrors());
    }

    @Test
    @DisplayName("Should return 400 Bad Request when MethodArgumentTypeMismatchException occurs")
    void whenMethodArgumentTypeMismatchException_thenReturnBadRequest() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getPropertyName()).thenReturn("field");
        when(webRequest.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getRequestURI()).thenReturn("/test-path");

        ResponseEntity<Object> response = restExceptionHandler.processTypeMismatchError(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        RestExceptionHandler.Error error = (RestExceptionHandler.Error) response.getBody();
        assertNotNull(error);
        assertEquals(2, error.getErrors().size());
        assertEquals("FieldName: field", error.getErrors().getFirst());
    }

    @Test
    @DisplayName("Should return correct status when HttpClientErrorException occurs")
    void whenHttpClientErrorException_thenReturnCorrectStatus() {
        HttpClientErrorException exception = mock(HttpClientErrorException.class);
        when(exception.getStatusText()).thenReturn("Not Found");
        when(exception.getStatusCode()).thenReturn(HttpStatusCode.valueOf(404));
        when(webRequest.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getRequestURI()).thenReturn("/test-path");

        ResponseEntity<Object> response = restExceptionHandler.processHttpClientError(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        RestExceptionHandler.Error error = (RestExceptionHandler.Error) response.getBody();
        assertNotNull(error);
        assertEquals("Message: Not Found", error.getErrors().getFirst());
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error when unhandled exception occurs")
    void whenUnHandleException_thenReturnInternalServerError() {
        Exception exception = new Exception("Unexpected error", new Throwable("Cause message"));
        when(webRequest.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getRequestURI()).thenReturn("/test-path");

        ResponseEntity<Object> response = restExceptionHandler.processUnHandleError(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        RestExceptionHandler.Error error = (RestExceptionHandler.Error) response.getBody();
        assertNotNull(error);
        assertEquals("Message: Unexpected error", error.getErrors().getFirst());
        assertEquals("Cause  : Cause message", error.getErrors().get(1));
    }

    @Test
    @DisplayName("Should return 500 with default messages when unhandled exception has no message/cause")
    void whenUnHandleExceptionWithoutMessageAndCause_thenReturnInternalServerErrorWithDefaults() {
        Exception exception = new Exception((String) null, null);
        when(webRequest.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getRequestURI()).thenReturn("/test-path");

        ResponseEntity<Object> response = restExceptionHandler.processUnHandleError(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        RestExceptionHandler.Error error = (RestExceptionHandler.Error) response.getBody();
        assertNotNull(error);
        assertEquals("Message: <empty>", error.getErrors().getFirst());
        assertEquals("Cause  : <unknown>", error.getErrors().get(1));
    }

    @Test
    @DisplayName("Should return 404 Not Found when RecordNotFoundException occurs")
    void whenRecordNotFoundException_thenReturnNotFound() {
        RecordNotFoundException exception = new RecordNotFoundException("Record not found");
        when(webRequest.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getRequestURI()).thenReturn("/test-path");

        ResponseEntity<Object> response = restExceptionHandler.handleNotFound(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        RestExceptionHandler.Error error = (RestExceptionHandler.Error) response.getBody();
        assertNotNull(error);
        assertEquals("Message: Record not found", error.getErrors().getFirst());
    }
}