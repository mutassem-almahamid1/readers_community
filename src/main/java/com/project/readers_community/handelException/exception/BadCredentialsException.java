package com.project.readers_community.handelException.exception;

import com.project.readers_community.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class BadCredentialsException extends ApiBaseException {
    public BadCredentialsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {

        return HttpStatus.UNAUTHORIZED;

    }
}
