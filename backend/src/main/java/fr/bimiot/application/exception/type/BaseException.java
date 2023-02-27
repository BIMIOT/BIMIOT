package fr.bimiot.application.exception.type;

import lombok.Data;

@Data
public class BaseException extends Exception {

    private final String code;

    public BaseException(String message, String code){
        super(message);
        this.code = code;
    }
}
