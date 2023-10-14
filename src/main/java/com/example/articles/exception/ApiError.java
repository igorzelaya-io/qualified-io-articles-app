package com.example.articles.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@JsonSerialize
public class ApiError {

    @JsonProperty
    private HttpStatus httpStatus;

    @JsonProperty
    private String msg;

    @JsonProperty
    private String debugMessage;

    @JsonProperty
    private List<SubError> subErrors;
    @JsonProperty
    private LocalDateTime date;

    public ApiError() {
        this.date = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus) {
        this();
        this.httpStatus = httpStatus;
    }

    public ApiError(HttpStatus httpStatus, Throwable ex){
        this();
        this.httpStatus = httpStatus;
        this.msg = "Unexpected error.";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus httpStatus, String msg, Throwable ex) {
        this();
        this.httpStatus = httpStatus;
        this.msg = msg;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus httpStatus, String msg, String debugMessage) {
        this.httpStatus = httpStatus;
        this.msg = msg;
        this.debugMessage = debugMessage;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public List<SubError> getValidationErrors() {
        return this.subErrors;
    }

    public void setValidationErrors(List<SubError> validationErrors) {
        this.subErrors = validationErrors;
    }

    private void addSubError(SubError subError) {
        this.subErrors.add(subError);
    }
    private void addValidationError(String object, String message) {
        this.addSubError(new ValidationError(object, message));
    }
    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        this.addSubError(new ValidationError(object, field, rejectedValue, message));
    }
    private void addValidationError(FieldError fieldError) {
        this.addValidationError
                (fieldError.getObjectName(), fieldError.getField(),
                        fieldError.getRejectedValue(), fieldError.getDefaultMessage());
    }
    private void addValidationError(ObjectError objectError) {
        this.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
    }
    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors
                .forEach(error -> addValidationError(error));
    }
    public void addValidationError(List<ObjectError> objectErrors) {
        objectErrors
                .forEach(error -> addValidationError(error));
    }

    private void addValidationError(ConstraintViolation<?> constraintViolation) {
        this.addValidationError(constraintViolation.getRootBeanClass().getSimpleName(),
                constraintViolation.getPropertyPath().toString(),
                constraintViolation.getInvalidValue(),
                constraintViolation.getMessage());
    }
    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations
                .forEach(violation -> addValidationError(violation));
    }
}

