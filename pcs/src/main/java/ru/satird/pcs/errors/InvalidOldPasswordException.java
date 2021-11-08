package ru.satird.pcs.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class InvalidOldPasswordException extends RuntimeException {

    private static final long serialVersionUID = -6408235072962206416L;
    public InvalidOldPasswordException() {
        super();
    }
    public InvalidOldPasswordException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public InvalidOldPasswordException(final String message) {
        super(message);
    }
    public InvalidOldPasswordException(final Throwable cause) {
        super(cause);
    }
}
