
dependencies {
    implementation("org.springframework:spring-tx")
    api(project(":common:common-domain"))
    api(project(":payment-service:payment-domain:payment-domain-core"))
    api(project(":infrastructure:outbox"))
    api(project(":infrastructure:saga"))

    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-test")
}

