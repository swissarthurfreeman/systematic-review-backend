package ch.unige.pinfo3.api.rest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ch.unige.pinfo3.utils.ErrorReport;

@Provider
public class ExceptionToUserMapper implements ExceptionMapper<ConstraintViolationException> {
    /**
     * This function will be invoked upon any constraintViolationException. 
     * @param exception contains all info on what violations were done. 
     * @return It'll return a Response that is served to the user.  
     */
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(getErrorFrom(exception)).build();
    }

    private ErrorReport getErrorFrom(ConstraintViolationException ex) {
        ErrorReport err = new ErrorReport();
        for(ConstraintViolation<?> cv: ex.getConstraintViolations())
            err.errors.add(new ErrorReport.Error(cv.getPropertyPath().toString(), cv.getMessage(), Response.Status.NOT_ACCEPTABLE));
        return err;
    }
}
