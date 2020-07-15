package docker.compose;

import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

public class DockerCompose extends DockerComposeContainer<DockerCompose> {

    public DockerCompose(File composeName) {
        super(composeName);
        this.withLocalCompose(true);
    }
}
