package infrastructure.model.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Setter
@Getter
@Accessors(fluent = true)
@JsonInclude(NON_NULL)
@JsonPropertyOrder({
        "selenium-hub",
        "chrome",
        "firefox",
        "selenoid",
        "selenoid-ui"
})
public class ServicesModel {
    @JsonProperty("selenium-hub")
    private ServiceModel seleniumHub;

    @JsonProperty("chrome")
    private ServiceModel chrome;

    @JsonProperty("firefox")
    private ServiceModel firefox;

    @JsonProperty("selenoid")
    private ServiceModel selenoid;

    @JsonProperty("selenoid-ui")
    private ServiceModel selenoidUi;
}