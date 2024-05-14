package com.xeridia.resource;

import com.xeridia.service.ERC20Service;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

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

    @POST
    @Path("/mint")
    public RestResponse mint(MintRequest mintRequest) throws Exception {
        erc20Service.mint(mintRequest.getAmount());
        return RestResponse.ok();
    }
}
