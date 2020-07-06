package extension;

import infrastructure.compose.ComposableEnvironment;
import infrastructure.compose.SelenoidComposer;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

import static com.codeborne.selenide.Configuration.baseUrl;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static webdriver.WebDriverFactory.*;

public class SelenoidExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, CloseableResource  {
    private static final String DOCKER_COMPOSE_SOURCE = "./src/test/resources/selenoid.yaml";
    private static final ComposableEnvironment ENVIRONMENT = new SelenoidComposer();
    private static boolean systemReady = false;

    synchronized private static void environmentSetup() {
        if (!systemReady) {
            ENVIRONMENT.start(DOCKER_COMPOSE_SOURCE);
            checkInstanceState(ENVIRONMENT.getLocalInstance("4444"));
            baseUrl = "https://the-internet.herokuapp.com";
            systemReady = true;
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        environmentSetup();
        context.getRoot().getStore(GLOBAL).put(systemReady, this);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        createDriverInstance(ENVIRONMENT.getLocalInstance("4444"));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        shutdownDriverInstance();
    }

    @Override
    public void close() {
        ENVIRONMENT.stop();
    }
}
