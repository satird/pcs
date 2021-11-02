package ru.satird.pcs.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdsNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7120208532984058644L;

    public AdsNotFoundException() {
        super();
    }

    public AdsNotFoundException(String message) {
        super(message);
    }

    public AdsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdsNotFoundException(Throwable cause) {
        super(cause);
    }

    protected AdsNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
