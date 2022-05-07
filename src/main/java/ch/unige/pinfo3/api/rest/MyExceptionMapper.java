package ch.unige.pinfo3.api.rest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MyExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(getErrorFrom(exception)).build();
    }

    private ErrorReport getErrorFrom(ConstraintViolationException ex) {
        ErrorReport err = new ErrorReport();
        for(ConstraintViolation<?> cv: ex.getConstraintViolations())
            err.errors.add(new Error(cv.getPropertyPath().toString(), cv.getMessage(), Response.Status.NOT_ACCEPTABLE));

        System.out.println("HERRREEE");
        System.out.println(err.help);
        return err;
    }
}
