package infrastructure.environment;

import docker.client.DockerCLI;
import infrastructure.model.environment.EnvironmentModel;

import java.io.File;

import static infrastructure.YAMLObjectMapper.getYaml;
import static java.lang.String.format;
import static java.time.Duration.ofSeconds;
import static org.awaitility.Awaitility.await;

public interface ComposableEnvironment {

    EnvironmentModel buildEnvironment();

    void start();

    void stop();

    default void pull(final DockerCLI client, final String image, final String tag) {
        client.pull(image, tag);
    }

    default File getCompose(final String  file) {
        return getYaml(new File(file), buildEnvironment());
    }

    default File getCompose(final String file, final EnvironmentModel environment) {
        return getYaml(new File(file), environment);
    }

    default String getLocalInstance(final String servicePort) {
        return format("http://0.0.0.0:%s/wd/hub", servicePort);
    }

    default void isHubRunning(final DockerCLI client, final String serviceName) {
        await().until(() -> client.warmingUp(serviceName, ofSeconds(5)));
    }
}
