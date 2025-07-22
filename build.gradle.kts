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
val restAssuredVersion = "5.5.5"
val junitBomVersion = "5.11.3"
val kotlinVersion = "2.1.0"
val logbackVersion = "1.5.3"
val slf4jVersion = "2.0.17"
val jacksonDatabindVersion = "2.19.2"
val cucumberVersion = "7.26.0"
val jacksonDatatypeJSR310Version = "2.19.2"

val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}


dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonDatabindVersion")

    // AspectJ weaver
    agent("org.aspectj:aspectjweaver:${aspectJVersion}")


    // Cucumber BOM i dependencies
    testImplementation(platform("io.cucumber:cucumber-bom:$cucumberVersion"))
    testImplementation("io.cucumber:cucumber-junit-platform-engine")
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-picocontainer")

    testImplementation("io.qameta.allure:allure-cucumber7-jvm")
    testImplementation("org.junit.platform:junit-platform-suite:1.10.2")
    testImplementation("org.apache.commons:commons-collections4:4.4")

    // Allure BOM i dependencies
    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-rest-assured:$allureVersion")
    testImplementation("io.qameta.allure:allure-junit5:$allureVersion")

    //testImplementation(platform("org.junit:junit-bom:5.9.1"))
    //testImplementation("org.junit.jupiter:junit-jupiter")

    // JUnit Platform
    testImplementation(platform("org.junit:junit-bom:$junitBomVersion"))
    testImplementation("org.junit.platform:junit-platform-suite")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // Kotlin support
    testImplementation(platform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion"))
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // Rest-Assured dependencies
    testImplementation("io.rest-assured:rest-assured:${restAssuredVersion}")
    testImplementation("io.rest-assured:kotlin-extensions:${restAssuredVersion}")
    testImplementation("io.rest-assured:json-path:${restAssuredVersion}")
    testImplementation("io.rest-assured:xml-path:${restAssuredVersion}")

    testImplementation("com.fasterxml.jackson.core:jackson-databind:${jacksonDatabindVersion}")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonDatatypeJSR310Version}")
    testImplementation("io.rest-assured:json-schema-validator:${restAssuredVersion}")
    testImplementation("org.json:json:20240205")

    // WireMock dependencies
    testImplementation("org.wiremock:wiremock:${wireMockVersion}")
    testImplementation("org.wiremock:wiremock-standalone:${wireMockVersion}")

    // Logowanie
    testImplementation("ch.qos.logback:logback-classic:${logbackVersion}")
    testImplementation("org.slf4j:slf4j-api:${slf4jVersion}")

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
/*
        // Forward system properties from command line to test JVM
        // Safe casting with type filtering
        systemProperties = System.getProperties()
            .filterKeys { it is String }
            .mapKeys { it.key as String }
            .mapValues { it.value ?: "" }
       // systemProperties = System.getProperties().toMap() as Map<String, Any>

        // Or specify individual properties
        systemProperty("api.mock", System.getProperty("api.mock", "false"))
        systemProperty("api.baseUri", System.getProperty("api.baseUri", "http://localhost"))
        systemProperty("api.port", System.getProperty("api.port", "8080"))
*/

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
