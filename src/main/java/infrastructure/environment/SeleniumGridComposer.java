package infrastructure.environment;

import docker.compose.DockerCompose;
import infrastructure.model.environment.EnvironmentModel;
import infrastructure.model.service.ServiceModel;
import infrastructure.model.service.ServicesModel;

import static java.util.Arrays.asList;

public class SeleniumGridComposer implements ComposableEnvironment {
    private static final String SELENIUM_HUB = "selenium-hub";
    private static final String SELENIUM_HUB_IMAGE = "selenium/hub:latest";
    private static final String SELENIUM_CHROME_IMAGE = "selenium/node-chrome:latest";
    private static final String SELENIUM_FIREFOX_IMAGE = "selenium/node-firefox:latest";
    private static final String HUB_HOST = "HUB_HOST=selenium-hub";
    private static final String HUB_PORT = "HUB_PORT=4444";

    private final DockerCompose dockerCompose;

    public SeleniumGridComposer(final String composeName) {
        this.dockerCompose = new DockerCompose(getCompose(composeName));
    }

    @Override
    public void start() {
        this.dockerCompose.withScaledService("chrome", 5);
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
                                .chrome(
                                        new ServiceModel()
                                                .image(SELENIUM_CHROME_IMAGE)
                                                .environment(asList(
                                                        HUB_HOST,
                                                        HUB_PORT
                                                ))
                                                .dependsOn(asList(SELENIUM_HUB))
                                )
                                .firefox(
                                        new ServiceModel()
                                                .image(SELENIUM_FIREFOX_IMAGE)
                                                .environment(asList(
                                                        HUB_HOST,
                                                        HUB_PORT
                                                ))
                                                .dependsOn(asList(SELENIUM_HUB))
                                )
                                .seleniumHub(
                                        new ServiceModel()
                                                .image(SELENIUM_HUB_IMAGE)
                                                .ports(asList("4445:4444"))));
    }
}
