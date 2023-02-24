package fr.bimiot.dataproviders.exception;

import fr.bimiot.entrypoints.exception.type.BaseException;

public class DataBaseException extends BaseException {

    public DataBaseException(String message, String code) {
        super(message, code);
    }
}
