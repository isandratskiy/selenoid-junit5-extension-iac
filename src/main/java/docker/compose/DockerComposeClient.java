package docker.compose;

import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

public class DockerComposeClient extends DockerComposeContainer<DockerComposeClient> {
    private static final String COMPOSE_NAME = "docker-compose.yaml";
    private static final String SERVICE_NAME = "selenium-hub";
    private static final String CHROME_NAME = "chrome";
    private static final String FIREFOX_NAME = "firefox";
    private static final int SERVICE_PORT = 4444;

    public DockerComposeClient(File composeFile) {
        super(composeFile);
        this.withLocalCompose(true);
    }
}
