package com.brunoams.accesscontrol.exception;

public class PasswordInvalidException extends RuntimeException{
    public PasswordInvalidException(String m) {
        super(m);
    }
}
