plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // https://mvnrepository.com/artifact/io.rest-assured/rest-assured
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    // https://mvnrepository.com/artifact/io.rest-assured/json-schema-validator
    testImplementation("io.rest-assured:json-schema-validator:5.4.0")
    // https://mvnrepository.com/artifact/org.json/json
    testImplementation("org.json:json:20240205")




}

tasks.test {
    useJUnitPlatform()
}