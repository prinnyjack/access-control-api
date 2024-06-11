package com.brunoams.accesscontrol.exception;

public class InvalidAuthorityException extends RuntimeException{
    public InvalidAuthorityException(String msg) {
        super(msg);
    }
}
