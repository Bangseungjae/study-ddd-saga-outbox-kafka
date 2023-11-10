
dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    implementation(project(":customer-service:customer-dataaccess"))
    implementation(project(":customer-service:customer-application"))
    implementation(project(":customer-service:customer-domain:customer-application-service"))
    implementation(project(":customer-service:customer-domain:customer-domain-core"))
    implementation(project(":customer-service:customer-messaging"))
}
