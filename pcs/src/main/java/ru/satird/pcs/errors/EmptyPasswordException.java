package ru.satird.pcs.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyPasswordException extends RuntimeException {

    private static final long serialVersionUID = 3910498398511835368L;

    public EmptyPasswordException() {
        super();
    }

    public EmptyPasswordException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmptyPasswordException(final String message) {
        super(message);
    }

    public EmptyPasswordException(final Throwable cause) {
        super(cause);
    }
}
