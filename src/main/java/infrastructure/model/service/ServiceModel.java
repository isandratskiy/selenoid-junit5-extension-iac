package infrastructure.model.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Setter
@Getter
@Accessors(fluent = true)
@JsonInclude(NON_NULL)
@JsonPropertyOrder({
        "container_name",
        "network_mode",
        "ports",
        "image",
        "volumes",
        "depends_on",
        "environment",
        "links",
        "command"
})
public class ServiceModel {
    @JsonProperty("container_name")
    private String containerName;

    @JsonProperty("network_mode")
    private String networkMode;

    @JsonProperty("ports")
    private List<String> ports;

    @JsonProperty("image")
    private String image;

    @JsonProperty("volumes")
    private List<String> volumes;

    @JsonProperty("depends_on")
    private List<String> dependsOn;

    @JsonProperty("environment")
    private List<String> environment;

    @JsonProperty("links")
    private List<String> links;

    @JsonProperty("command")
    private List<String> command;
}
