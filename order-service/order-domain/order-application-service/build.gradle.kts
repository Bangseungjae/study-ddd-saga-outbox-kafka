dependencies {
    implementation(project(":order-service:order-domain:order-domain-core"))
    implementation(project(":common:common-domain"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework:spring-tx")
}
