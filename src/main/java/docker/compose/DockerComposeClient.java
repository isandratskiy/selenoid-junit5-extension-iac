package docker.compose;

import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

public class DockerComposeClient extends DockerComposeContainer<DockerComposeClient> {
    public DockerComposeClient(File composeName) {
        super(composeName);
        this.withLocalCompose(true);
    }
}
