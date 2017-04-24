package com.msis.common.service;

public class ServiceException extends Exception {
    private ServiceStatus status;
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(ServiceStatus status) {
        this.status = status;
    }
    public ServiceException(ServiceStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ServiceStatus getStatus() {
        return status;
    }
}
