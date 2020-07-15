package infrastructure.environment;

import docker.client.DockerCLI;
import docker.compose.DockerCompose;
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

    private final DockerCompose dockerCompose;

    public SelenoidComposer(final String composeName) {
        this.dockerCompose = new DockerCompose(getCompose(composeName));
    }

    public void pullChrome() {
        System.out.println(":::::::::::::::\n PULL CHROME \n:::::::::::::::");
        pull(DOCKER_CLI,"selenoid/vnc_chrome", "83.0");
    }

    public void pullFirefox() {
        System.out.println(":::::::::::::::\n PULL FIREFOX \n:::::::::::::::");
        pull(DOCKER_CLI,"selenoid/vnc_firefox", "78.0");
    }

    @Override
    public void start() {
        this.pullChrome();
        this.pullFirefox();
        this.dockerCompose.start();
    }

    @Override
    public void stop() {
        this.dockerCompose.stop();
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
                                                        ".:/etc/selenoid", //$PWD
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
                                                .setPorts(asList("8080:8080"))));
    }
}
