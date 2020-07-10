package infrastructure.environment;

import docker.client.DockerCLI;
import docker.compose.DockerComposeClient;
import infrastructure.model.environment.EnvironmentModel;
import infrastructure.model.service.ServiceModel;
import infrastructure.model.service.ServicesModel;

import static java.util.Arrays.asList;

public class SelenoidComposer implements ComposableEnvironment {
    private static final DockerCLI DOCKER_CLI = new DockerCLI();
    private static final String SELENOID = "selenoid";
    private static final String BRIDGE = "bridge";
    private static final String SELENOID_IMAGE = "aerokube/selenoid:latest-release";
    private static final String SELENOID_UI_IMAGE = "aerokube/selenoid-ui:latest-release";

    private final DockerComposeClient dockerComposeClient;

    public SelenoidComposer(String composeName) {
        this.dockerComposeClient = new DockerComposeClient(getCompose(composeName));
    }

    @Override
    public void pullChrome() {
        DOCKER_CLI.pull("selenoid/vnc_chrome", "83.0");
    }

    @Override
    public void pullFirefox() {
        DOCKER_CLI.pull("selenoid/vnc_firefox", "78.0");
    }

    @Override
    public void start() {
        this.dockerComposeClient.start();
    }

    @Override
    public void stop() {
        this.dockerComposeClient.stop();
    }

    @Override
    public EnvironmentModel buildEnvironment() {
        return new EnvironmentModel()
                .setVersion("3.4")
                .setServices(
                        new ServicesModel()
                                .setSelenoid(
                                        new ServiceModel()
                                                .setNetworkMode(BRIDGE)
                                                .setImage(SELENOID_IMAGE)
                                                .setVolumes(asList(
                                                        "$PWD:/etc/selenoid",
                                                        "/var/run/docker.sock:/var/run/docker.sock"
                                                ))
                                                .setCommand(asList(
                                                        "-conf",
                                                        "/etc/selenoid/browsers.json",
                                                        "-service-startup-timeout",
                                                        "5m0s",
                                                        "-session-attempt-timeout",
                                                        "5m0s",
                                                        "-session-delete-timeout",
                                                        "5m0s",
                                                        "-timeout",
                                                        "5m0s"
                                                ))
                                                .setPorts(asList("4444:4444"))
                                )
                                .setSelenoidUi(
                                        new ServiceModel()
                                                .setNetworkMode(BRIDGE)
                                                .setImage(SELENOID_UI_IMAGE)
                                                .setDependsOn(asList(SELENOID))
                                                .setLinks(asList(SELENOID))
                                                .setCommand(asList(
                                                        "--selenoid-uri",
                                                        "http://selenoid:4444"
                                                ))
                                                .setPorts(asList("8081:8080"))
                                )
                );
    }
}
