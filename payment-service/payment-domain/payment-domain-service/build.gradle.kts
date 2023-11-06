
dependencies {
    implementation("org.springframework:spring-tx")
    api(project(":common:common-domain"))
    api(project(":payment-service:payment-domain:payment-domain-core"))
}

