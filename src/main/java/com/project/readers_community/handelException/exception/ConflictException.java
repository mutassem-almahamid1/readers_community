package com.project.readers_community.handelException.exception;

import com.project.readers_community.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends ApiBaseException {
    public ConflictException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.CONFLICT;
    }
}
