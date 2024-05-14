package com.xeridia;

import com.xeridia.solidity.HelloWorld;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

@ApplicationScoped
public class GreetingService {

    @ConfigProperty(name = "blockchain.network")
    String blockchainNetwork;

    @ConfigProperty(name = "blockchain.account.privateKey")
    String blockchainAccountPrivateKey;

    private static String contractAddress;

    public String deploy() throws Exception {
        Credentials credentials = Credentials.create(blockchainAccountPrivateKey);
        Web3j web3j = Web3j.build(new HttpService(blockchainNetwork));
        HelloWorld helloWorld = HelloWorld.deploy(web3j, credentials, new DefaultGasProvider()).send();
        contractAddress = helloWorld.getContractAddress();
        return contractAddress;
    }

    public String hello(String name) throws Exception {
        Credentials credentials = Credentials.create(blockchainAccountPrivateKey);
        Web3j web3j = Web3j.build(new HttpService(blockchainNetwork));
        HelloWorld helloWorld = HelloWorld.load(contractAddress, web3j, credentials, new DefaultGasProvider());
        return helloWorld.sayHello(name).send();
    }
}
