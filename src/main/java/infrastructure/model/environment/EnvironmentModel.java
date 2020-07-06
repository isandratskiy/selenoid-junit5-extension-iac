package infrastructure.model.environment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import infrastructure.model.service.ServicesModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Setter
@Getter
@Accessors(chain = true)
@JsonInclude(NON_NULL)
@JsonPropertyOrder({
        "version",
        "services"
})
public class EnvironmentModel {
    @JsonProperty("version")
    private String version;

    @JsonProperty("services")
    private ServicesModel services;
}
