package fr.bimiot.domain.exception;

import fr.bimiot.application.exception.type.BaseException;

public class DomainException extends BaseException {

    private final static String CODE = "400";

    public DomainException(String message) {
        super(message, CODE);
    }

}
