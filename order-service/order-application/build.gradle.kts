

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("commons-logging:commons-logging")
    }
    implementation("org.springframework.boot:spring-boot-starter-validation")

    api(project(":order-service:order-domain:order-application-service"))
    api(project(":order-service:order-domain:order-domain-core"))
    api(project(":common:common-domain"))
    api(project(":common:common-application"))
}

tasks.named<Jar>("jar") {
    enabled = true
}
