import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.jpa") version "1.8.22"
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}


dependencies {

    // JPA
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    api(project(":common:common-domain"))
    api(project(":restaurant-service:restaurant-domain:restaurant-application-service"))

    // Database
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.6.0")

    implementation(project(":common:common-dataaccess"))

}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
