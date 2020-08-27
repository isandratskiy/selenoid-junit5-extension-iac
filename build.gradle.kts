import java.lang.System.getProperty

plugins {
    java
    id("io.freefair.lombok") version "4.1.6"
}

group = "sandratskyi.io"
version = "1.0.0"

repositories {
    jcenter()
    mavenCentral()
}

object Version {
    const val FLOGGER = "0.5.1"
    const val JUNIT = "5.6.2"
    const val APACHE_COMMONS = "3.9"
    const val JACKSON = "2.10.3"
    const val TESTCONTAINERS = "1.15.0-rc1"
    const val SELENIDE = "5.13.0"
    const val DOCKER_JAVA = "3.2.5"
    const val SNAKEYAML = "1.26"
    const val JACKSON_DATAFORMAT = "2.11.0"
    const val AWAITILITY = "4.0.2"
    const val SLF4J = "1.7.30"
    const val OWNER = "1.0.10"
}

dependencies {
    runtimeOnly("org.slf4j", "slf4j-simple", Version.SLF4J)
    implementation("org.aeonbits.owner", "owner", Version.OWNER)
    implementation("com.google.flogger", "flogger-system-backend", Version.FLOGGER)
    implementation("com.google.flogger", "flogger", Version.FLOGGER)
    implementation("org.apache.commons", "commons-lang3", Version.APACHE_COMMONS)
    implementation("org.testcontainers", "testcontainers", Version.TESTCONTAINERS)
    implementation("com.codeborne", "selenide", Version.SELENIDE)
    implementation("com.github.docker-java", "docker-java", Version.DOCKER_JAVA)
    implementation("org.yaml", "snakeyaml", Version.SNAKEYAML)
    implementation("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", Version.JACKSON_DATAFORMAT)
    implementation("com.fasterxml.jackson.core", "jackson-databind", Version.JACKSON)
    implementation("org.awaitility", "awaitility", Version.AWAITILITY)
    testImplementation("org.testcontainers", "junit-jupiter", Version.TESTCONTAINERS)
    testImplementation("org.junit.jupiter", "junit-jupiter", Version.JUNIT)
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    register<Test>("testSelenoid") {
        useJUnitPlatform {
            includeTags = setOf("selenoid")
        }
        systemProperties["environment.config"] = "remote"
    }

    register<Test>("testSelenium") {
        useJUnitPlatform {
            includeTags = setOf("selenium")
        }
        systemProperties["environment.config"] = "remote"
    }

    withType(Test::class) {

        testLogging {
            showStackTraces = false
            showExceptions = true
            showStandardStreams = true
            events("passed", "failed")
        }

        systemProperties["browser"] = getProperty("browser") ?:"chrome"
    }
}