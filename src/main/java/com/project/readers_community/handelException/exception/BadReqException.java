package com.project.readers_community.handelException.exception;

import com.project.readers_community.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class BadReqException extends ApiBaseException {

    public BadReqException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
