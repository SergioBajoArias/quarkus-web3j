package com.xeridia.config;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {
        org.web3j.protocol.core.Request.class,
        org.web3j.protocol.core.Response.class,
        org.web3j.protocol.deserializer.KeepAsJsonDeserialzier.class,
        org.web3j.protocol.core.methods.response.EthGetTransactionCount.class,
        org.web3j.protocol.core.methods.response.EthSendTransaction.class,
        org.web3j.protocol.core.methods.response.EthGetTransactionReceipt.class,
        org.web3j.protocol.core.methods.response.TransactionReceipt.class,
        org.web3j.protocol.core.methods.response.EthCall.class,
        org.web3j.protocol.core.methods.request.Transaction.class,
        org.web3j.abi.datatypes.generated.Uint256.class,
        org.web3j.protocol.core.DefaultBlockParameterName.class,
        org.web3j.protocol.core.DefaultBlockParameterNumber.class
})
public class ReflectionConfig {
}
