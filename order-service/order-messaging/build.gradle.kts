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

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
    implementation(project(mapOf("path" to ":infrastructure:kafka:kafka-model")))
    testImplementation("org.springframework.kafka:spring-kafka-test")

    implementation("org.apache.avro:avro:${avroVersion}")
    implementation("org.apache.avro:avro-tools:${avroVersion}")
}

tasks.named<BootJar>("bootJar") {
//    mainClass = "com.food.ordering.system.Application"
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
