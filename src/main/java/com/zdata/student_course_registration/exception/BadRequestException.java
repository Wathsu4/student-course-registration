package com.zdata.student_course_registration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // This annotation automatically sets the HTTP status code
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
