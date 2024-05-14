package com.xeridia;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @Inject
    public GreetingService greetingService;

    @POST
    @Path("/blockchain")
    @Produces(MediaType.TEXT_PLAIN)
    public String deploy() throws Exception {
        return greetingService.deploy();
    }

    @GET
    @Path("/blockchain/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@PathParam("name") String name) throws Exception {
        return greetingService.hello(name);
    }
}
