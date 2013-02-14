package br.com.bea.androidtools.api.service;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceException(final String message) {
        super(message);
    }

    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServiceException(final Throwable cause) {
        super(cause);
    }

}
