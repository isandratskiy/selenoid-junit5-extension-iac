package infrastructure.compose;

import docker.client.DockerCLI;
import infrastructure.model.environment.EnvironmentModel;

import java.io.File;

import static infrastructure.YAMLObjectMapper.getYaml;
import static java.lang.String.format;
import static java.time.Duration.ofSeconds;
import static org.awaitility.Awaitility.await;

public interface ComposableEnvironment {

    EnvironmentModel buildEnvironment();

    void start(String composeSource);

    void stop();

    default File compose(File file) {
        return getYaml(file, buildEnvironment());
    }

    default File compose(File file, EnvironmentModel environment) {
        return getYaml(file, environment);
    }

    default String getLocalInstance(String servicePort) {
        return format("http://0.0.0.0:%s/wd/hub", servicePort);
    }

    default void isHubRunning(DockerCLI client, String serviceName) {
        await().until(() -> client.warmingUp(serviceName, ofSeconds(5)));
    }
}
