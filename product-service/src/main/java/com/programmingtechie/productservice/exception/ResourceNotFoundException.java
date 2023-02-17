package com.programmingtechie.productservice.exception;

public class ResourceNotFoundException extends RuntimeException{
    private String message;

    public ResourceNotFoundException(String s){
        super(s);
    }
}
