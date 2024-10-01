package com.xeridia;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GanacheTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {

    private static final String GANACHE_IMAGE = "trufflesuite/ganache";

    public static final int GANACHE_PORT = 8545;

    private Optional<String> containerNetworkId = Optional.empty();

    private DockerComposeContainer dockerComposeContainer;

    private GenericContainer container;

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        containerNetworkId = context.containerNetworkId();
    }

    @SuppressWarnings("resource")
    @Override
    public Map<String, String> start() {
        String blockchainNetwork;
        if(containerNetworkId.isPresent()) {
            dockerComposeContainer = new DockerComposeContainer(
                    new File("src/test/resources/docker-compose-ganache.yml")
            ).withLocalCompose(false);

            dockerComposeContainer.start();

            blockchainNetwork = "http://ganache:" + GANACHE_PORT;
        } else {
            container = new GenericContainer(GANACHE_IMAGE)
                    .withCommand("-e 999999999999999999 --wallet.deterministic true")
                    .withExposedPorts(GANACHE_PORT)
                    .waitingFor(Wait.forListeningPort());

            container.start();

            blockchainNetwork = "http://" + container.getHost() + ":" + container.getMappedPort(GANACHE_PORT);
        }
        log.info("Blockchain network is {}", blockchainNetwork);

        return Map.of(
                "blockchain.network", blockchainNetwork
        );
    }

    @Override
    public void stop() {
        if(this.container != null) {
            this.container.stop();
        }
        if(this.dockerComposeContainer != null) {
            this.dockerComposeContainer.stop();
        }
    }
}