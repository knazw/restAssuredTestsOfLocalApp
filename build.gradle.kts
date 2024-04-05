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


}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf(
            "-javaagent:${agent.singleFile}"
    )
}