import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

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

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
configurations {
    all {
        exclude("commons-logging:commons-logging")
        exclude("org.slf4j:slf4j-reload4j")
    }
}
