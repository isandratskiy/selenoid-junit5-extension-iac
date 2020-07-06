package docker.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Container;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.List;

import static com.github.dockerjava.core.DockerClientBuilder.getInstance;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DockerCLI {
    private final DockerClient client;

    public DockerCLI() {
        this.client = getInstance().build();
    }

    public List<Container> getContainers() {
        return this.client.listContainersCmd()
                .withStatusFilter(asList("running"))
                .exec();
    }

    public Container getContainerByName(String name) {
        return this.getContainers().stream()
                .filter(container -> container.getNames()[0].contains(name))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found container by name: " + name));
    }

    @SneakyThrows
    public void pull(String image, String tag) {
        this.client.pullImageCmd(image)
                .withTag(tag)
                .exec(new PullImageResultCallback())
                .awaitCompletion(120, SECONDS);
    }

    public boolean isRunning(String name) {
        return this.getContainerByName(name).getState().equals("running");
    }

    public boolean warmingUp(String containerName, Duration timer) {
        return this.getContainerByName(containerName).getStatus().contains(valueOf(timer.getSeconds()));
    }

    @SneakyThrows
    public void close() {
        this.client.close();
    }
}
