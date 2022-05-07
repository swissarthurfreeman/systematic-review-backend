package ch.unige.pinfo3.api.rest;
import javax.ws.rs.core.Response.Status;;

public record Error(String cause, String message, Status status) {};

