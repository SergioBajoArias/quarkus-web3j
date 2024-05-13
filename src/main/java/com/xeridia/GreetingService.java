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

    @ConfigProperty(name = "blockchain.network.host")
    String blockchainNetworkHost;

    @ConfigProperty(name = "blockchain.network.port")
    int blockchainNetworkPort;

    private String blockchainConnectionChain = "http://" + blockchainNetworkHost + ":" + blockchainNetworkPort;
    public String deploy() throws Exception {
        Credentials credentials = Credentials.create("0xafcec06329da0bdfa387c63102a4a8ea58ebc8d849136824217d8fb310587e1c");

        Web3j web3j = Web3j.build(new HttpService(blockchainConnectionChain));
        HelloWorld helloWorld = HelloWorld.deploy(web3j, credentials, new DefaultGasProvider()).send();
        return helloWorld.getContractAddress();
    }
}
