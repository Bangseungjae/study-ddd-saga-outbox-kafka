import org.springframework.boot.gradle.tasks.bundling.BootJar

val avroVersion = "1.11.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    api(project(":order-service:order-domain:order-application-service"))
    api(project(":infrastructure:kafka:kafka-consumer"))
    api(project(":infrastructure:kafka:kafka-producer"))
    api(project(":common:common-application"))
    api(project(":common:common-domain"))
    api(project(":order-service:order-domain:order-domain-core"))

    implementation("org.springframework.boot:spring-boot-starter-json")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
    implementation(project(mapOf("path" to ":infrastructure:kafka:kafka-model")))
    testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
