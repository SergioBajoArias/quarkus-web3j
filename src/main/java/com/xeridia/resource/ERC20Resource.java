package com.xeridia.resource;

import com.xeridia.service.ERC20Service;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

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
    public TransactionReceipt mint(MintRequest mintRequest) throws Exception {
        return erc20Service.mint(mintRequest.getAmount());
    }

    @POST
    @Path("/burn")
    public TransactionReceipt burn(BurnRequest burnRequest) throws Exception {
        try {
            return erc20Service.burn(burnRequest.getAmount());
        } catch (TransactionException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
