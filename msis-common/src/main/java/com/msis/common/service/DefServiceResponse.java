package com.msis.common.service;

public class DefServiceResponse<S> {
    private ServiceStatus status;
    private S result;

    public DefServiceResponse(){
    }

    public DefServiceResponse(S result) {
        this.result = result;
    }

    public DefServiceResponse(ServiceStatus status) {
        this.status = status;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public S getResult() {
        return result;
    }

    public void setResult(S result) {
        this.result = result;
    }

    public boolean isSuccess() {
        if (status == null || status.getCode() == ServiceStatus.OK.getCode()) {
            return true;
        }
        return false;
    }
}
