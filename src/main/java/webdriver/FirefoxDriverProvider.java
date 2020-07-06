package webdriver;

import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class FirefoxDriverProvider {

    public static FirefoxOptions getOptions() {
        var options = new FirefoxOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-web-security");
        options.setAcceptInsecureCerts(true);
        options.setHeadless(false);
        return options;
    }

    static class Remote implements WebDriverProvider {
        private static URL instance;

        @Override
        public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
            return new RemoteWebDriver(instance, getOptions());
        }
    }
}
