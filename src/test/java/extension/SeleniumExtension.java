package extension;

import infrastructure.environment.ComposableEnvironment;
import infrastructure.environment.SeleniumGridComposer;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.codeborne.selenide.Configuration.baseUrl;
import static extension.Environment.SELENIUM_GRID;
import static infrastructure.Logger.logInfo;
import static infrastructure.configuration.Configuration.buildConfiguration;
import static infrastructure.configuration.SeleniumConfigurationProvider.getComposePath;
import static infrastructure.configuration.SeleniumConfigurationProvider.getSeleniumPort;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import static webdriver.WebDriverFactory.*;

public class SeleniumExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, CloseableResource {
    private static final Namespace NAMESPACE = create(SELENIUM_GRID);
    private static ComposableEnvironment ENVIRONMENT;
    private static boolean gridReady = false;

    synchronized private static void environmentSetup() {
        if (!gridReady) {
            buildConfiguration();
            logInfo("::::::::::::::: SETUP GRID ENVIRONMENT :::::::::::::::");
            ENVIRONMENT = new SeleniumGridComposer(getComposePath());
            ENVIRONMENT.start();
            checkInstanceState(ENVIRONMENT.getLocalInstance(getSeleniumPort()));
            gridReady = true;
            logInfo("::::::::::::::: GRID ENVIRONMENT READY :::::::::::::::");
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        environmentSetup();
        context.getRoot().getStore(NAMESPACE).put(gridReady, this);
        baseUrl = "https://the-internet.herokuapp.com";
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        createDriverInstance(ENVIRONMENT.getLocalInstance(getSeleniumPort()));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        shutdownDriverInstance();
    }

    @Override
    public void close() {
        ENVIRONMENT.stop();
        logInfo(":::::::::::::: SHUTDOWN GRID ENVIRONMENT ::::::::::::::");
    }
}
