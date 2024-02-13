package com.springboot.restapi.appilaction.expensetrackerapi.exception;

public class EtBadRequestException extends RuntimeException{
    
    public EtBadRequestException(String message){
        super(message);
    }
}
