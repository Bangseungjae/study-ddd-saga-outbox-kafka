import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":payment-service:payment-domain:payment-domain-core"))
    implementation(project(":payment-service:payment-domain:payment-domain-service"))
    implementation(project(":payment-service:payment-dataaccess"))
    implementation(project(":payment-service:payment-messaging"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
