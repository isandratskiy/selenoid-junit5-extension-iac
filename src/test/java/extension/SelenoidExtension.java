package extension;

import infrastructure.environment.ComposableEnvironment;
import infrastructure.environment.SelenoidComposer;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

import static com.codeborne.selenide.Configuration.baseUrl;
import static docker.Logger.logInfo;
import static extension.Environment.SELENOID;
import static infrastructure.configuration.Configuration.buildConfiguration;
import static infrastructure.configuration.SelenoidConfigurationProvider.getComposePath;
import static infrastructure.configuration.SelenoidConfigurationProvider.getSelenoidPort;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static webdriver.WebDriverFactory.*;

public class SelenoidExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, CloseableResource {
    private static final Namespace NAMESPACE = create(SELENOID);
    private static ComposableEnvironment ENVIRONMENT;
    private static boolean selenoidReady = false;

    synchronized private static void environmentSetup() {
        if (!selenoidReady) {
            buildConfiguration();
            logInfo("::::::::::::::: SETUP SELENOID ENVIRONMENT :::::::::::::::");
            ENVIRONMENT = new SelenoidComposer(getComposePath());
            ENVIRONMENT.start();
            checkInstanceState(ENVIRONMENT.getLocalInstance(getSelenoidPort()));
            selenoidReady = true;
            logInfo("::::::::::::::: SELENOID ENVIRONMENT READY :::::::::::::::");
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        environmentSetup();
        context.getRoot().getStore(NAMESPACE).put(selenoidReady, this);
        baseUrl = "https://the-internet.herokuapp.com";
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        createDriverInstance(ENVIRONMENT.getLocalInstance(getSelenoidPort()));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        shutdownDriverInstance();
    }

    @Override
    public void close() {
        ENVIRONMENT.stop();
        logInfo(":::::::::::::: SHUTDOWN SELENOID ENVIRONMENT ::::::::::::::");
    }
}
