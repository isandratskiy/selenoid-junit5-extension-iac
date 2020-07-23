package extension;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Tag("selenium")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Execution(CONCURRENT)
@ExtendWith(SeleniumExtension.class)
public @interface StartSeleniumEnvironment {
}
