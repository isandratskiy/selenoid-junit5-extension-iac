package infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import infrastructure.model.environment.EnvironmentModel;
import lombok.SneakyThrows;

import java.io.File;

import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.*;

public final class YAMLObjectMapper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(
            new YAMLFactory()
                    .disable(MINIMIZE_QUOTES)
                    .enable(INDENT_ARRAYS)
                    .enable(USE_PLATFORM_LINE_BREAKS)
                    .disable(WRITE_DOC_START_MARKER)
    );

    private YAMLObjectMapper() {

    }

    @SneakyThrows
    public static File getYaml(File file, EnvironmentModel model) {
        OBJECT_MAPPER.writeValue(file, model);
        return file;
    }
}
