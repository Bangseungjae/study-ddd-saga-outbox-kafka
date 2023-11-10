
dependencies {
    api(project(":customer-service:customer-domain:customer-domain-core"))
    api(project(":common:common-domain"))

    implementation("org.springframework:spring-tx")
}
