package com.xeridia.config;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {
        org.web3j.protocol.core.Request.class,
        org.web3j.protocol.core.methods.response.EthGetTransactionCount.class
})
public class ReflectionConfig {
}
