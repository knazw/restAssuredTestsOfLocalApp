plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val allureVersion = "2.26.0"
val aspectJVersion = "1.9.21"

val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}


dependencies {
    implementation("com.google.code.gson:gson:2.10.1")



    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    testImplementation("io.rest-assured:json-schema-validator:5.4.0")
    testImplementation("org.json:json:20240205")

    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-rest-assured")
    testImplementation("io.qameta.allure:allure-junit5")

    agent("org.aspectj:aspectjweaver:${aspectJVersion}")

    testImplementation("io.cucumber:cucumber-bom:7.16.1")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.16.1")
    testImplementation("io.cucumber:cucumber-java:7.16.1")
    testImplementation("io.qameta.allure:allure-cucumber7-jvm")
    testImplementation("org.junit.platform:junit-platform-suite:1.10.2")
    testImplementation("org.apache.commons:commons-collections4:4.4")

    testImplementation("io.cucumber:cucumber-picocontainer:7.16.1")


    testImplementation("org.slf4j:slf4j-api:2.0.9")
    testImplementation("ch.qos.logback:logback-classic:1.5.3")

}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf(
            "-javaagent:${agent.singleFile}"
    )
}