package ch.unige.pinfo3.domain.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/")
@RegisterRestClient
public interface JobClient {
    @GET
    String get();
}