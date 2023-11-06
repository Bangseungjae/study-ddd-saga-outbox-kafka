import org.springframework.boot.gradle.tasks.bundling.BootJar


dependencies {
    api(project(":order-service:order-domain:order-domain-core"))
    api(project(":infrastructure:kafka:kafka-config-data"))
    api(project(":infrastructure:kafka:kafka-model"))
    api(project(":common:common-domain"))
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.kafka:spring-kafka")
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
