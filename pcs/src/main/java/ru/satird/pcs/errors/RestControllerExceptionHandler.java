package ru.satird.pcs.errors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messages;
    private static final String ERROR_NOT_FOUND = "404 Status Code";

    public RestControllerExceptionHandler() {
        super();
    }

    @Autowired
    public void setMessages(@Qualifier("messageSource") MessageSource messages) {
        this.messages = messages;
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }


    @ExceptionHandler(value = LoadUserByUsernameException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleLoadUserByUsernameException(TokenRefreshException ex, WebRequest request) {
        return new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(value = EmptyPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleEmptyPasswordException(TokenRefreshException ex, WebRequest request) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(value = InvalidOldPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInvalidOldPasswordException(TokenRefreshException ex, WebRequest request) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleUserNotFound(RuntimeException ex, WebRequest request) {
        logger.error(ERROR_NOT_FOUND, ex);
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(value = AdsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleAdsNotFound(RuntimeException ex, WebRequest request) {
        logger.error(ERROR_NOT_FOUND, ex);
        return new ErrorMessage(messages.getMessage("Ads not found", null, request.getLocale()), HttpStatus.NOT_FOUND.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler({MailAuthenticationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleMail(RuntimeException ex, WebRequest request) {
        logger.error("500 Status Code", ex);
        return new ErrorMessage(messages.getMessage("message.newLoc.enabled", null, request.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler({ UserAlreadyExistException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleUserAlreadyExist(final RuntimeException ex, final WebRequest request) {
        logger.error("409 Status Code", ex);
        return new ErrorMessage(messages.getMessage("message.regError", null, request.getLocale()), HttpStatus.CONFLICT.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleInternal(RuntimeException ex, WebRequest request) {
        logger.error(ERROR_NOT_FOUND, ex);
        return new ErrorMessage(messages.getMessage("message.error", null, request.getLocale()), HttpStatus.NOT_FOUND.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error("400 Status Code", ex);
        final BindingResult result = ex.getBindingResult();
        final ErrorMessage errorMessage = new ErrorMessage(messages.getMessage("Invalid" + result.getObjectName(), null, request.getLocale()), HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage(), request.getDescription(false));
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error("400 Status Code", ex);
        final BindingResult result = ex.getBindingResult();
        final ErrorMessage errorMessage = new ErrorMessage(messages.getMessage("Invalid" + result.getObjectName(), null, request.getLocale()), HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage(), request.getDescription(false));
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, request);
    }
}
