package com.xeridia.service;

import com.xeridia.solidity.ERC20;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

@ApplicationScoped
@Slf4j
public class ERC20Service {

    @ConfigProperty(name = "blockchain.network")
    String blockchainNetwork;

    @ConfigProperty(name = "blockchain.account.privateKey")
    String blockchainAccountPrivateKey;

    private ERC20 erc20;

    @Startup
    public void deploy() throws Exception {
        Credentials credentials = Credentials.create(blockchainAccountPrivateKey);
        Web3j web3j = Web3j.build(new HttpService(blockchainNetwork));
        String contractAddress = getContractAddress();
        if(contractAddress == null) {
            erc20 = ERC20.deploy(web3j, credentials, new DefaultGasProvider()).send();
        } else {
            erc20 = ERC20.load(contractAddress, web3j, credentials, new DefaultGasProvider());
        }
    }

    private String getContractAddress() {
        // TODO: In a real scenario, the contract address should be persisted in order to be accessed again
        return null;
    }

    public BigInteger balanceOf() throws Exception {
        return erc20.balanceOf().send();
    }

    public TransactionReceipt mint(Integer amount) throws Exception {
        log.info("Minting {} units", amount);
        return erc20.mint(BigInteger.valueOf(amount)).send();
    }

    public TransactionReceipt burn(Integer amount) throws Exception {
        log.info("Burning {} units", amount);
        return erc20.burn(BigInteger.valueOf(amount)).send();
    }
}
