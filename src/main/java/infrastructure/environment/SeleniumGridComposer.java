package infrastructure.environment;

import docker.compose.DockerComposeClient;
import infrastructure.model.environment.EnvironmentModel;
import infrastructure.model.service.ServiceModel;
import infrastructure.model.service.ServicesModel;

import static java.util.Arrays.asList;

public class SeleniumGridComposer implements ComposableEnvironment {
    private static final String SELENIUM_HUB_IMAGE = "selenium/hub:latest";
    private static final String SELENIUM_CHROME_IMAGE = "selenium/node-chrome:latest";
    private static final String SELENIUM_FIREFOX_IMAGE = "selenium/node-firefox:latest";

    private final DockerComposeClient dockerComposeClient;

    public SeleniumGridComposer(final String composeName) {
        this.dockerComposeClient = new DockerComposeClient(getCompose(composeName));
    }

    @Override
    public void start() {
        this.dockerComposeClient.withScaledService("chrome", 4);
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
                                .setChrome(
                                        new ServiceModel()
                                                .setImage(SELENIUM_CHROME_IMAGE)
                                                .setEnvironment(asList(
                                                        "HUB_HOST=selenium-hub",
                                                        "HUB_PORT=4444"
                                                ))
                                                .setDependsOn(asList(
                                                        "selenium-hub"
                                                ))
                                )
                                .setFirefox(
                                        new ServiceModel()
                                                .setImage(SELENIUM_FIREFOX_IMAGE)
                                                .setEnvironment(asList(
                                                        "HUB_HOST=selenium-hub",
                                                        "HUB_PORT=4444"
                                                ))
                                                .setDependsOn(asList(
                                                        "selenium-hub"
                                                ))
                                )
                                .setSeleniumHub(
                                        new ServiceModel()
                                                .setImage(SELENIUM_HUB_IMAGE)
                                                .setPorts(asList(
                                                        "4445:4444"
                                                ))
                                )
                );
    }
}
