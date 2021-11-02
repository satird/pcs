package ru.satird.pcs.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public final class LoadUserByUsernameException extends RuntimeException {

    private static final long serialVersionUID = -5300486420556433712L;

    public LoadUserByUsernameException() {
        super();
    }

    public LoadUserByUsernameException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LoadUserByUsernameException(final String message) {
        super(message);
    }

    public LoadUserByUsernameException(final Throwable cause) {
        super(cause);
    }
}
