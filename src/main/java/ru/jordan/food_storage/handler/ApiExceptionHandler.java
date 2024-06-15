package ru.jordan.food_storage.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.jordan.food_storage.model.Error.ApiError;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(HttpServletRequest request, Exception ex) {
        return createError(request, ex);
    }

    @ExceptionHandler({
            BadRequestException.class,
            NoHandlerFoundException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestExceptions(HttpServletRequest request, RuntimeException ex) {
        return createError(request, ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException ex) {
        return createError(request, ex);
    }

    private ApiError createError(HttpServletRequest request, Exception ex) {
        UUID errorId = UUID.randomUUID();
        String requestInfo = request.getMethod() + " " + request.getRequestURI();
        log.error("Error " + errorId + " processing request " + requestInfo, ex);
        ApiError error = new ApiError();
        error.setDetails(ex.toString());
        error.setErrorCode("API_ERROR");
        error.setId(errorId);
        error.setRequest(requestInfo);
        error.setDatetime(OffsetDateTime.now());
        return error;
    }
}
