package com.xeridia.service;

import com.xeridia.solidity.ERC20;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

@ApplicationScoped
public class ERC20Service {

    @ConfigProperty(name = "blockchain.network")
    String blockchainNetwork;

    @ConfigProperty(name = "blockchain.account.privateKey")
    String blockchainAccountPrivateKey;

    private static String contractAddress;

    private String deploy() throws Exception {
        Credentials credentials = Credentials.create(blockchainAccountPrivateKey);
        Web3j web3j = Web3j.build(new HttpService(blockchainNetwork));
        ERC20 erc20 = ERC20.deploy(web3j, credentials, new DefaultGasProvider()).send();
        contractAddress = erc20.getContractAddress();
        return contractAddress;
    }

    public BigInteger balanceOf() throws Exception {
        Credentials credentials = Credentials.create(blockchainAccountPrivateKey);
        Web3j web3j = Web3j.build(new HttpService(blockchainNetwork));
        if(contractAddress == null) {
            deploy();
        }
        ERC20 erc20 = ERC20.load(contractAddress, web3j, credentials, new DefaultGasProvider());
        return erc20.balanceOf().send();
    }

    public void mint(Integer amount) throws Exception {
        Credentials credentials = Credentials.create(blockchainAccountPrivateKey);
        Web3j web3j = Web3j.build(new HttpService(blockchainNetwork));
        if(contractAddress == null) {
            deploy();
        }
        ERC20 erc20 = ERC20.load(contractAddress, web3j, credentials, new DefaultGasProvider());
        erc20.mint(BigInteger.valueOf(amount)).send();
    }
}
