
dependencies {
    api(project(":common:common-application"))
    api(project(":customer-service:customer-domain:customer-application-service"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}
