package extension;

import infrastructure.environment.ComposableEnvironment;
import infrastructure.environment.SelenoidComposer;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

import static com.codeborne.selenide.Configuration.baseUrl;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static webdriver.WebDriverFactory.*;

public class SelenoidExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, CloseableResource {
    private static final ComposableEnvironment ENVIRONMENT = new SelenoidComposer("./src/test/resources/selenoid-compose.yaml");
    private static boolean selenoidReady = false;

    synchronized private static void environmentSetup() {
        if (!selenoidReady) {
            System.out.println(":::::::::::::::\n SETUP SELENOID ENVIRONMENT \n:::::::::::::::");
            ENVIRONMENT.start();
            checkInstanceState(ENVIRONMENT.getLocalInstance("4444"));
            baseUrl = "https://the-internet.herokuapp.com";
            selenoidReady = true;
            System.out.println(":::::::::::::::\n SELENOID ENVIRONMENT READY \n:::::::::::::::");
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        environmentSetup();
        context.getRoot().getStore(GLOBAL).put(selenoidReady, this);
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
        System.out.println("::::::::::::::\n SHUTDOWN SELENOID ENVIRONMENT \n::::::::::::::");
    }
}
