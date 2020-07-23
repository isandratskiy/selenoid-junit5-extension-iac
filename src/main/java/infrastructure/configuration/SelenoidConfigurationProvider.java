package infrastructure.configuration;

import static org.aeonbits.owner.ConfigFactory.create;

public final class SelenoidConfigurationProvider {
    private static final EnvironmentConfiguration CONFIGURATION = create(EnvironmentConfiguration.class);

    private SelenoidConfigurationProvider() {
    }

    public static String getSelenoidPort() {
        return CONFIGURATION.getSelenoidPort();
    }

    public static String getSelenoidUiPort() {
        return CONFIGURATION.getSelenoidUiPort();
    }

    public static String getComposePath() {
        return CONFIGURATION.getSelenoidComposePath();
    }

    public static String getChromeVersion() {
        return CONFIGURATION.getSelenoidChromeVersion();
    }

    public static String getFirefoxVersion() {
        return CONFIGURATION.getSelenoidFirefoxVersion();
    }
}
