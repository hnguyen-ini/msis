package com.msis.common.service;

public class ServiceStatus {
    final static public ServiceStatus OK = new ServiceStatus(200, "OK");
    final static public ServiceStatus ACCEPTED = new ServiceStatus(202, "Accepted");
    final static public ServiceStatus NO_CONTENT = new ServiceStatus(204, "No content");
    
    final static public ServiceStatus NOT_MODIFIED = new ServiceStatus(304, "Not Modified");
    
    final static public ServiceStatus BAD_REQUEST = new ServiceStatus(400, "Bad request");
    final static public ServiceStatus UNAUTHORIZED = new ServiceStatus(401, "Unauthorized");
    final static public ServiceStatus PAYMENT_REQUIRED = new ServiceStatus(402, "Payment Required");
    final static public ServiceStatus FORBIDDEN = new ServiceStatus(403, "Forbidden");
    final static public ServiceStatus NOT_FOUND = new ServiceStatus(404, "Not found");
    final static public ServiceStatus METHOD_NOT_ALLOWED = new ServiceStatus(405, "Method Not Allowed");
    final static public ServiceStatus NOT_ACCEPTABLE = new ServiceStatus(406, "Not Acceptable");
    final static public ServiceStatus REQUEST_TIME_OUT = new ServiceStatus(408, "Request Time Out");
    final static public ServiceStatus OVER_INSTOCK = new ServiceStatus(409, "Over InStock");
    
    final static public ServiceStatus SERVICE_ERROR = new ServiceStatus(500, "Internal server error");
    final static public ServiceStatus SERVER_CONNECTION = new ServiceStatus(501, "Server connection error");
    final static public ServiceStatus RUNNING_TIME_ERROR = new ServiceStatus(502, "Running time trror");
    final static public ServiceStatus SERVER_UNAVAILABLE = new ServiceStatus(503, "Server unavailable");
    final static public ServiceStatus DUPLICATE_USER = new ServiceStatus(504, "Duplicate user email");
    public static final ServiceStatus INACTIVE_USER = new ServiceStatus(505, "Inactive user");
    final static public ServiceStatus DUPLICATE_PATIENT = new ServiceStatus(504, "Duplicate patient");
    public static final ServiceStatus CRYPTO_ERROR = new ServiceStatus(508, "Crypto error");
    public static final ServiceStatus BAD_PASSWORD = new ServiceStatus(507, "Password mismatch"); 
    
    private int code;
    private String status;

    public ServiceStatus() {
        this.code = OK.code;
        this.status = OK.status;
    }

    public ServiceStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public ServiceStatus(ServiceStatus serviceStatus, String message) {
        this.code = serviceStatus.getCode();
        this.status = serviceStatus.getStatus();
        if (message != null && !message.trim().isEmpty()) {
            this.status = message;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ServiceStatus){
            ServiceStatus that = (ServiceStatus)other;
            return this.code==that.code;
        }
        return false;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
}
