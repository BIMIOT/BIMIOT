package fr.bimiot.application.exception.type;

import lombok.Data;

@Data
public class BaseException extends Exception {

    private String code = "500";

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, String code) {
        this(message);
        this.code = code;
    }
}
