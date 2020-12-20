package webdriver;

import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Map;

import static java.util.List.of;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class ChromeDriverProvider {
    public static ChromeOptions getOptions() {
        var options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.setCapability("noProxy", true);
        options.setCapability("enableVNC", true);
        options.setCapability("enableVideo", false);
        options.setExperimentalOption("excludeSwitches", of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("prefs", disablePasswordManager());
        options.setAcceptInsecureCerts(true);
        options.setHeadless(false);
        return options;
    }

    private static Map<String, Boolean> disablePasswordManager() {
        return ofEntries(
                entry("credentials_enable_service", false),
                entry("profile.password_manager_enabled", false));
    }

    static class Remote implements WebDriverProvider {
        private static URL instance;

        @Override
        public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
            return new RemoteWebDriver(instance, getOptions());
        }
    }
}
