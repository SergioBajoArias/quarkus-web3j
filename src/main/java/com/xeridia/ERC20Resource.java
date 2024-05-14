package com.xeridia;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.math.BigInteger;

@Path("/erc20")
public class ERC20Resource {

    @Inject
    public ERC20Service erc20Service;

    @GET
    @Path("/balanceOf")
    @Produces(MediaType.TEXT_PLAIN)
    public BigInteger balanceOf() throws Exception {
        return erc20Service.balanceOf();
    }
}
