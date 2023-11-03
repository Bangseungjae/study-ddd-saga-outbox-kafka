import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    api(project(":payment-service:payment-domain:payment-domain-service"))
    api(project(":infrastructure:kafka:kafka-consumer"))
    api(project(":infrastructure:kafka:kafka-producer"))
    api(project(":infrastructure:kafka:kafka-model"))
}


tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
