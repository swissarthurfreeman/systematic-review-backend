package ch.unige.pinfo3.utils;

import java.util.ArrayList;
import javax.ws.rs.core.Response.Status;

public class ErrorReport {
    public record Error(String cause, String message, Status status) {};
    public final String help = "You are big silly orang-utang";
    public ArrayList<Error> errors = new ArrayList<>();
}
