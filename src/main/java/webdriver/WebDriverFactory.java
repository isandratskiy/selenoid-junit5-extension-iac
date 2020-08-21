package webdriver;

import lombok.SneakyThrows;

import java.net.URL;

import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static infrastructure.configuration.Configuration.getBrowserProperty;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.rnorth.ducttape.unreliables.Unreliables.retryUntilSuccess;

public final class WebDriverFactory {
    private WebDriverFactory() {
    }

    public static void createDriverInstance(String instance) {
        switch (getBrowserProperty()) {
            case "chrome":
                Browser.REMOTE_CHROME.start(instance);
                break;
            case "firefox":
                Browser.REMOTE_FIREFOX.start(instance);
                break;
            default: throw new IllegalStateException("Unknown browser type " + getBrowserProperty());
        }
    }

    public static void checkInstanceState(String instance) {
        var fakedriver = retryUntilSuccess(60, SECONDS, () -> {
            createDriverInstance(instance);
            open();
            return getWebDriver();
        });
        fakedriver.quit();
    }

    public static void shutdownDriverInstance() {
        getWebDriver().quit();
    }

    private enum Browser {
        REMOTE_CHROME {
            @Override
            void start(String instance) {
                var clazz = ChromeDriverProvider.Remote.class;
                setRemoteInstance(clazz, instance);
                browser = clazz.getName();
            }
        },
        REMOTE_FIREFOX {
            @Override
            void start(String instance) {
                var clazz = FirefoxDriverProvider.Remote.class;
                setRemoteInstance(clazz, instance);
                browser = clazz.getName();
            }
        };

        abstract void start(String instance);
    }

    @SneakyThrows
    private static void setRemoteInstance(Class<?> clazz, String instance) {
        var field = clazz.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, new URL(instance));
    }
}
