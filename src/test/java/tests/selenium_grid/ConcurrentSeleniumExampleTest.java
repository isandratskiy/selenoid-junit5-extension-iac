package tests.selenium_grid;

import extension.StartSeleniumEnvironment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.AuthenticationType.BASIC;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.tagName;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

@StartSeleniumEnvironment
@DisplayName("Concurrent Selenium Grid tests")
public class ConcurrentSeleniumExampleTest {

    @Test
    void canEditIFrameText() {
        var inputText = randomAlphabetic(200);
        open("/");
        $$(".columns ul > li a").find(matchText("WYSIWYG")).scrollTo().click();
        switchTo().frame("mce_0_ifr");
        var editor = $(".mce-content-body");
        editor.clear();
        editor.val(inputText);
        editor.shouldHave(exactText(inputText));
    }

    @Test
    void canPassBasicAuth() {
        open("/basic_auth", BASIC, "admin", "admin");
        $(tagName("p")).shouldHave(exactText("Congratulations! You must have the proper credentials."));
    }

    @Test
    void canPassForgotPassword() {
        open("/forgot_password");
        $("#email").val(randomAlphabetic(5) + "@icloude.com");
        $("#form_submit").click();
        $("#content").shouldHave(exactText("Your e-mail's been sent!"));
    }

    @Test
    void canLoginWithCredentials() {
        open("/login");
        $("#username").val("tomsmith");
        $("#password").val("SuperSecretPassword!");
        $(".radius").click();
        $(".flash").shouldHave(matchesText("You logged into a secure area!"));
    }
}
