package infrastructure.configuration;

import static org.aeonbits.owner.ConfigFactory.create;

public final class SeleniumConfigurationProvider {
    private static final EnvironmentConfiguration CONFIGURATION = create(EnvironmentConfiguration.class);

    private SeleniumConfigurationProvider() {
    }

    public static String getSeleniumPort() {
        return CONFIGURATION.getSeleniumPort();
    }

    public static String getComposePath() {
        return CONFIGURATION.getSeleniumComposePath();
    }

    public static String getChromeVersion() {
        return CONFIGURATION.getSeleniumChromeVersion();
    }

    public static String getFirefoxVersion() {
        return CONFIGURATION.getSeleniumFirefoxVersion();
    }
}
