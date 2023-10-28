

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(project(":order-service:order-domain:order-application-service"))
    implementation(project(":order-service:order-domain:order-domain-core"))
    implementation(project(":common:common-domain"))
    implementation(project(":common:common-application"))
    implementation(project(":common:common-application"))
}
