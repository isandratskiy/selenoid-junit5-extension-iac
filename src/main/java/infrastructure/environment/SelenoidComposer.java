package infrastructure.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import docker.client.DockerCLI;
import docker.compose.DockerCompose;
import infrastructure.model.environment.EnvironmentModel;
import infrastructure.model.service.ServiceModel;
import infrastructure.model.service.ServicesModel;
import lombok.SneakyThrows;

import static infrastructure.Logger.logInfo;
import static infrastructure.configuration.SelenoidConfigurationProvider.*;
import static java.nio.file.Files.write;
import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.List.of;

public class SelenoidComposer implements ComposableEnvironment {
    private static final DockerCLI DOCKER_CLI = new DockerCLI();
    private static final String SELENOID = "selenoid";
    private static final String BRIDGE = "bridge";
    private static final String SELENOID_IMAGE = "aerokube/selenoid:latest-release";
    private static final String SELENOID_UI_IMAGE = "aerokube/selenoid-ui:latest-release";

    private final DockerCompose dockerCompose;

    public SelenoidComposer(final String composeName) {
        this.buildBrowsersJson();
        this.dockerCompose = new DockerCompose(getCompose(composeName));
    }

    public void pullChrome() {
        logInfo("::::::::::::::: PULL CHROME :::::::::::::::");
        this.pull(DOCKER_CLI, "selenoid/vnc_chrome", getChromeVersion());
    }

    public void pullFirefox() {
        logInfo("::::::::::::::: PULL FIREFOX ::::::::::::::");
        this.pull(DOCKER_CLI, "selenoid/vnc_firefox", getFirefoxVersion());
    }

    @SneakyThrows
    private void buildBrowsersJson() {
        var template = new ObjectMapper().readTree(get("src/main/resources/configuration/browsers-template.json").toFile()).toPrettyString();
        var result = template
                .replaceAll("CHROME.VERSION", getChromeVersion())
                .replaceAll("FIREFOX.VERSION", getFirefoxVersion());
        write(get(getBrowsersPath()), result.getBytes());
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
                                                .command(of(
                                                        "-conf",
                                                        "/etc/selenoid/browsers.json",
                                                        "-service-startup-timeout",
                                                        "6m0s",
                                                        "-session-attempt-timeout",
                                                        "6m0s",
                                                        "-session-delete-timeout",
                                                        "6m0s",
                                                        "-timeout",
                                                        "6m0s"
                                                ))
                                                .ports(of(
                                                        "{PORT}:4444".replace("{PORT}", getSelenoidPort()))
                                                )
                                )
                                .selenoidUi(
                                        new ServiceModel()
                                                .networkMode(BRIDGE)
                                                .image(SELENOID_UI_IMAGE)
                                                .dependsOn(of(SELENOID))
                                                .links(of(SELENOID))
                                                .command(of(
                                                        "--selenoid-uri",
                                                        "http://selenoid:4444")
                                                )
                                                .ports(of(
                                                        "{PORT}:8080".replace("{PORT}", getSelenoidUiPort()))
                                                )
                                )
                );
    }
}
