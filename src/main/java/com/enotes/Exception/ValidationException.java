package com.enotes.Exception;

import java.util.Map;

public class ValidationException extends RuntimeException {

    private final Map<String,Object> error;

    public ValidationException(Map<String,Object> error) {
        super("Validation Failed");
        this.error = error;
    }

    public Map<String,Object> getErrors(){
        return error;
    }

}
