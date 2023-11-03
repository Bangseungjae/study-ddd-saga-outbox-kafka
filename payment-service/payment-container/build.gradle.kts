
dependencies {
    implementation(project(":payment-service:payment-domain:payment-domain-core"))
    implementation(project(":payment-service:payment-domain:payment-domain-service"))
    implementation(project(":payment-service:payment-dataaccess"))
    implementation(project(":payment-service:payment-messaging"))

    implementation("org.springframework.boot:spring-boot-starter")
}

