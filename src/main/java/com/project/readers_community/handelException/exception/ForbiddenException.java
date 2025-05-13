package com.project.readers_community.handelException.exception;

import com.project.readers_community.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiBaseException {
    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.FORBIDDEN;
    }
}
