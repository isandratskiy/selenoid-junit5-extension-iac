package webdriver;

import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;

import static java.util.Collections.singletonList;

public class ChromeDriverProvider {

    public static ChromeOptions getOptions() {
        var options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.setCapability("noProxy", true);
        options.setCapability("enableVNC", true);
        options.setCapability("enableVideo", false);
        options.setExperimentalOption("excludeSwitches", singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("prefs", disablePasswordManager());
        options.setAcceptInsecureCerts(true);
        options.setHeadless(false);
        return options;
    }

    private static HashMap<String, Boolean> disablePasswordManager() {
        var preference = new HashMap<String, Boolean>();
        preference.put("credentials_enable_service", false);
        preference.put("profile.password_manager_enabled", false);
        return preference;
    }

    static class Remote implements WebDriverProvider {
        private static URL instance;

        @Override
        public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
            return new RemoteWebDriver(instance, getOptions());
        }
    }
}
