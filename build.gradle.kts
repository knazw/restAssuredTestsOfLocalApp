plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val allureVersion = "2.29.0"
val aspectJVersion = "1.9.24"
val wireMockVersion = "3.13.1"

val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}


dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")


    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    testImplementation("io.rest-assured:json-schema-validator:5.4.0")
    testImplementation("org.json:json:20240205")

    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-rest-assured:$allureVersion")
    testImplementation("io.qameta.allure:allure-junit5:$allureVersion")

    agent("org.aspectj:aspectjweaver:${aspectJVersion}")

    testImplementation("io.cucumber:cucumber-bom:7.16.1")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.16.1")
    testImplementation("io.cucumber:cucumber-java:7.16.1")
    testImplementation("io.qameta.allure:allure-cucumber7-jvm")
    testImplementation("org.junit.platform:junit-platform-suite:1.10.2")
    testImplementation("org.apache.commons:commons-collections4:4.4")

    testImplementation("io.cucumber:cucumber-picocontainer:7.26.0")


    testImplementation("org.slf4j:slf4j-api:2.0.17")
    testImplementation("ch.qos.logback:logback-classic:1.5.3")

    // WireMock dependencies
    testImplementation("org.wiremock:wiremock:${wireMockVersion}")
    testImplementation("org.wiremock:wiremock-standalone:${wireMockVersion}")

}

//tasks.test {
//    useJUnitPlatform()
//    jvmArgs = listOf(
//            "-javaagent:${agent.singleFile}"
//    )
//}
tasks {
    test {
        // REQUIRED: Tell Gradle to use the JUnit 5 platform to execute tests
        // see https://docs.gradle.org/current/userguide/java_testing.html#using_junit5
        useJUnitPlatform {
            // OPTIONAL: Exclude all tests (examples/scenarios) annotated with @disabled by default
            excludeTags("disabled")
            // OPTIONAL: Include only specified tags using JUnit5 tag expressions
            if (project.hasProperty("includeTags")) includeTags(project.property("includeTags") as String?)
        }
        // OPTIONAL: Ignore test failures so that build pipelines won't get blocked by failing examples/scenarios
        ignoreFailures = true
        // OPTIONAL: Copy all system properties from the command line (-D...) to the test environment
        systemProperties(project.gradle.startParameter.systemPropertiesArgs)
        // OPTIONAL: Enable parallel test execution
        systemProperty("cucumber.execution.parallel.enabled", false)
        // OPTIONAL: Set parallel execution strategy (defaults to dynamic)
        systemProperty("cucumber.execution.parallel.config.strategy", "fixed")
        // OPTIONAL: Set the fixed number of parallel test executions. Only works for the "fixed" strategy defined above
        systemProperty("cucumber.execution.parallel.config.fixed.parallelism", 4)
        // OPTIONAL: Enable Cucumber plugins, enable/disable as desired
        systemProperty("cucumber.plugin", "message:build/reports/cucumber.ndjson, timeline:build/reports/timeline, html:build/reports/cucumber.html")
        // OPTIONAL: Improve readability of test names in reports
        systemProperty("cucumber.junit-platform.naming-strategy", "long")
        // OPTIONAL: Don't show Cucumber ads
        systemProperty("cucumber.publish.quiet", "true")
        // OPTIONAL: Force test execution even if they are up-to-date according to Gradle or use "gradle test --rerun"
        outputs.upToDateWhen { false }
    }
}
