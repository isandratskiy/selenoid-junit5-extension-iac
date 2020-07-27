package webdriver;

import com.codeborne.selenide.WebDriverProvider;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class FirefoxDriverProvider {

    public static FirefoxOptions getOptions() {
        var options = new FirefoxOptions();
        options.setHeadless(false);
        options.setAcceptInsecureCerts(true);
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-web-security");
        options.setCapability("noProxy", true);
        options.setCapability("enableVNC", true);
        options.setCapability("enableVideo", false);
        return options;
    }

    static class Remote implements WebDriverProvider {
        private static URL instance;

        @Override
        public @NotNull WebDriver createDriver(@NotNull DesiredCapabilities desiredCapabilities) {
            return new RemoteWebDriver(instance, getOptions());
        }
    }
}
