package com.project.readers_community.handelException.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorDetails {

    private String message;

    private String uri;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date date;

    public ErrorDetails() {
        this.date = new Date();
    }


    public ErrorDetails(String message, String uri) {
        this();
        this.message = message;
        this.uri = uri;
    }

}