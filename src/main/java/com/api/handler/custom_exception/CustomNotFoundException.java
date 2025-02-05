package com.api.handler.custom_exception;

import lombok.Getter;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@Getter
public class CustomNotFoundException extends NotFoundException {
    private final String message;

    public CustomNotFoundException(String message){
        super();
        this.message = message;
    }
}
