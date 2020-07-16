package extension;

import infrastructure.environment.ComposableEnvironment;
import infrastructure.environment.SeleniumGridComposer;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.codeborne.selenide.Configuration.baseUrl;
import static extension.Environment.SELENIUM_GRID;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import static webdriver.WebDriverFactory.*;

public class SeleniumExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, CloseableResource {
    private static final ComposableEnvironment ENVIRONMENT = new SeleniumGridComposer("./src/test/resources/selenium-compose.yaml");
    private static final Namespace NAMESPACE = create(SELENIUM_GRID);
    private static boolean gridReady = false;

    synchronized private static void environmentSetup() {
        if (!gridReady) {
            System.out.println(":::::::::::::::\n SETUP GRID ENVIRONMENT \n:::::::::::::::");
            ENVIRONMENT.start();
            checkInstanceState(ENVIRONMENT.getLocalInstance("4445"));
            baseUrl = "https://the-internet.herokuapp.com";
            gridReady = true;
            System.out.println(":::::::::::::::\n GRID ENVIRONMENT READY \n:::::::::::::::");
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        environmentSetup();
        context.getRoot().getStore(NAMESPACE).put(gridReady, this);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        createDriverInstance(ENVIRONMENT.getLocalInstance("4445"));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        shutdownDriverInstance();
    }

    @Override
    public void close() {
        ENVIRONMENT.stop();
        System.out.println("::::::::::::::\n SHUTDOWN GRID ENVIRONMENT \n::::::::::::::");
    }
}
