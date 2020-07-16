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
        this.pull(DOCKER_CLI,"selenoid/vnc_chrome", "83.0");
    }

    public void pullFirefox() {
        System.out.println(":::::::::::::::\n PULL FIREFOX \n:::::::::::::::");
        this.pull(DOCKER_CLI,"selenoid/vnc_firefox", "78.0");
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
                .version("3.4")
                .services(
                        new ServicesModel()
                                .selenoid(
                                        new ServiceModel()
                                                .networkMode(BRIDGE)
                                                .image(SELENOID_IMAGE)
                                                .volumes(asList(
                                                        ".:/etc/selenoid", //$PWD
                                                        "/var/run/docker.sock:/var/run/docker.sock"
                                                ))
                                                .command(asList(
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
                                                .ports(asList("4444:4444"))
                                )
                                .selenoidUi(
                                        new ServiceModel()
                                                .networkMode(BRIDGE)
                                                .image(SELENOID_UI_IMAGE)
                                                .dependsOn(asList(SELENOID))
                                                .links(asList(SELENOID))
                                                .command(asList(
                                                        "--selenoid-uri",
                                                        "http://selenoid:4444"
                                                ))
                                                .ports(asList("8080:8080"))));
    }
}
