package ch.unige.pinfo3.api.rest;

import javax.ws.rs.core.Response.Status;

public class Error {
    final public String cause;
    final public String message;
    final public Status status;

    public Error(String cause, String message, Status status) {
        this.message = message;
        this.status = status;
        this.cause = cause;
    }
}
