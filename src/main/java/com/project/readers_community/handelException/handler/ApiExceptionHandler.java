package com.project.readers_community.handelException.handler;

import com.project.readers_community.handelException.ApiBaseException;
import com.project.readers_community.handelException.classes.ErrorDetails;
import com.project.readers_community.handelException.classes.ValidationError;
import com.project.readers_community.handelException.exception.BadReqException;
import com.project.readers_community.handelException.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ApiBaseException.class)
    public ResponseEntity<ErrorDetails> handleApiExceptions(@NotNull ApiBaseException ex, @NotNull WebRequest request) {
        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getStatusCode());
    }

//    @ExceptionHandler(BadReqException.class)
//    public ResponseEntity<ErrorDetails> handleApiExceptions(@NotNull BadReqException ex, @NotNull WebRequest request){
//        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
//        return new ResponseEntity<>(details, ex.getStatusCode());
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ErrorDetails> handleApiExceptions(@NotNull NotFoundException ex, @NotNull WebRequest request){
//        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
//        return new ResponseEntity<>(details, ex.getStatusCode());
//    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ValidationError validationError = new ValidationError();
        validationError.setUri(request.getDescription(false));

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            validationError.addError(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
    }
}
