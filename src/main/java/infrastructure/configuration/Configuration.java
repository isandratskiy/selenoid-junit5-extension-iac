package infrastructure.configuration;

import static infrastructure.Logger.logInfo;
import static java.lang.System.getProperty;
import static org.aeonbits.owner.ConfigFactory.setProperty;

public final class Configuration {
    private static final String INFRASTRUCTURE_PROP = "environment.config";
    private static final String BROWSER_PROPERTY = "browser";

    private Configuration() {
    }

    public static void buildConfiguration() {
        setInfrastructureConfig();
        logInfo("::::::::::::::: BUILD CONFIGURATION :::::::::::::::");
    }

    private static void setInfrastructureConfig() {
        setProperty(INFRASTRUCTURE_PROP, getInfrastructureProperty());
    }

    private static String getInfrastructureProperty() {
        return getProperty(INFRASTRUCTURE_PROP, "local");
    }

    public static String getBrowserProperty() {
        return getProperty(BROWSER_PROPERTY, "chrome");
    }
}
