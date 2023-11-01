
dependencies {
    api(project(":payment-service:payment-domain:payment-domain-service"))

    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    api(project(":common:common-domain"))
    implementation(project(":payment-service:payment-domain:payment-domain-core"))
}

