package com.example.articles.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex){
        ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST);
        errorResponse.setMsg("Validation error.");
        errorResponse.addValidationErrors(ex.getConstraintViolations());
        logger.error(errorResponse.getMsg());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException
            (IllegalArgumentException ex){
        ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST);
        errorResponse.setMsg(ex.getMessage());
        logger.error(errorResponse.getMsg());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex){
        logger.error(ex.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation
            (DataIntegrityViolationException ex, WebRequest request){
        logger.error(ex.getMessage());
        if(ex.getCause() instanceof ConstraintViolationException){
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch
            (MethodArgumentTypeMismatchException ex, WebRequest request){
        ApiError response = new ApiError(HttpStatus.NOT_FOUND);
        response.setMsg(String
                .format("The parameter %s of value '%s' could not be converted to type '%s'"
                        , ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));

        response.setDebugMessage(ex.getLocalizedMessage());
        logger.error(response.getMsg());
        return buildResponseEntity(response);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
                                                                     HttpStatusCode status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder(ex.getContentType().toString())
                .append(" media type is not supported. Supported media types are: ");
        ex.getSupportedMediaTypes()
                .forEach(mediaType -> stringBuilder.append(mediaType).append(", "));

        String msg = stringBuilder.substring(0, stringBuilder.length() - 2);
        logger.error(msg);

        return buildResponseEntity
                (new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, msg, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        logger.info(
                String.format("%s to %s"
                        , servletWebRequest.getHttpMethod().toString()
                        , servletWebRequest.getRequest().getServletPath()));
        String error = "Malformed JSON request";
        logger.error(error);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        String error = "Error writing JSON output";
        logger.error(error);
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatusCode status,
                                                                          WebRequest request) {
        String error = new StringBuilder(ex.getParameterName())
                .append(" parameter is name")
                .toString();
        logger.error(error);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {

        ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST);
        errorResponse.setMsg("Validation Error.");
        errorResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());
        errorResponse.addValidationError(ex.getBindingResult().getGlobalErrors());
        logger.error(errorResponse.getMsg());
        return buildResponseEntity(errorResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError errorResponse){
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }
}
