package infrastructure.configuration;

import static docker.Logger.logInfo;
import static java.lang.System.getProperty;
import static org.aeonbits.owner.ConfigFactory.setProperty;

public final class Configuration {
    private static final String INFRASTRUCTURE_PROP = "environment.config";

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
}
