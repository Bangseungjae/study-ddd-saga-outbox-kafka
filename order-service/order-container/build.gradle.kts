import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

dependencies {
    api(project(":order-service:order-domain:order-application-service"))
    api(project(":order-service:order-domain:order-domain-core"))
    api(project(":order-service:order-application"))
    api(project(":order-service:order-dataaccess"))
    api(project(":order-service:order-messaging"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks.getByName<BootBuildImage>("bootBuildImage") {
    imageName.set("bsj1209/order-container:latest")
    environment = mapOf("--platform" to "linux/arm64")
}

