package tests.selenium_grid;

import extension.StartSeleniumEnvironment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.AuthenticationType.BASIC;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.By.tagName;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

@StartSeleniumEnvironment
@DisplayName("Concurrent Selenium Grid tests")
public class ConcurrentSeleniumExampleTest {

    @Test
    void canEditIFrameText() {
        open("/");
        $$(".columns ul > li a").find(matchText("WYSIWYG")).scrollTo().click();
        switchTo().frame("mce_0_ifr");
        var editor = $(".mce-content-body");
        editor.clear();
        var text = randomAlphabetic(200);
        editor.val(text);
        assertEquals(text, editor.text());
    }

    @Test
    void canPassBasicAuth() {
        open("/basic_auth", BASIC, "admin", "admin");
        var actualText = element(tagName("p")).text();
        var expectedText = "Congratulations! You must have the proper credentials.";
        assertEquals(expectedText, actualText);
    }

    @Test
    void canPassForgotPassword() {
        open("/forgot_password");
        $("#email").val(randomAlphabetic(5) + "@icloude.com");
        $("#form_submit").click();
        assertEquals("Your e-mail's been sent!", $("#content").text().trim());
    }

    @Test
    void canLoginWithCredentials() {
        open("/login");
        $("#username").val("tomsmith");
        $("#password").val("SuperSecretPassword!");
        $(".radius").click();
        $(".flash success");
        assertTrue(element(".flash").text().contains("You logged into a secure area"));
    }
}
