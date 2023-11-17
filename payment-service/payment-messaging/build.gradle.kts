import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    api(project(":payment-service:payment-domain:payment-domain-service"))
    api(project(":infrastructure:kafka:kafka-consumer"))
    api(project(":infrastructure:kafka:kafka-producer"))
    api(project(":infrastructure:kafka:kafka-model"))
    api(project(":common:common-messaging"))


    implementation("org.postgresql:postgresql")
}


tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
