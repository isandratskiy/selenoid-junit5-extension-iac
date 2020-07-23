package infrastructure.configuration;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.Sources;

@Sources({"classpath:configuration/${environment.config}.properties"})
public interface EnvironmentConfiguration extends Config {
    @Key("selenoid-compose.source")
    String getSelenoidComposePath();

    @Key("selenoid.port")
    String getSelenoidPort();

    @Key("selenoid-ui.port")
    String getSelenoidUiPort();

    @Key("selenoid.chrome.version")
    String getSelenoidChromeVersion();

    @Key("selenoid.firefox.version")
    String getSelenoidFirefoxVersion();

    @Key("selenium-compose.source")
    String getSeleniumComposePath();

    @Key("selenium-grid.port")
    String getSeleniumPort();

    @Key("selenium.chrome.version")
    String getSeleniumChromeVersion();

    @Key("selenium.firefox.version")
    String getSeleniumFirefoxVersion();
}
