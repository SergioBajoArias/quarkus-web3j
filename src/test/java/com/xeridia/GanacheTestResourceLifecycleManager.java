package com.xeridia;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.restassured.RestAssured;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

import java.util.Map;

public class GanacheTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private static final String GANACHE_IMAGE = "trufflesuite/ganache";

    public static final int GANACHE_PORT = 8545;
    private GenericContainer container;

    @SuppressWarnings("resource")
    @Override
    public Map<String, String> start() {
        container = new GenericContainer(GANACHE_IMAGE)
                .withCommand("-e 999999999999999999 --wallet.deterministic true")
                .withExposedPorts(GANACHE_PORT)
                .waitingFor(Wait.forListeningPort());

        container.start();

        return Map.of(
                "blockchain.network", "http://" + container.getHost() + ":" + container.getMappedPort(GANACHE_PORT)
        );
    }

    @Override
    public void stop() {
        if(container != null) {
            container.stop();
        }
    }
}