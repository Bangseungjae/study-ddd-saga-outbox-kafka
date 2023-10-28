
plugins {

    id("io.kotest.multiplatform") version "5.0.2"
    kotlin("plugin.jpa") version "1.8.22"
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}


dependencies {
    implementation(project(":order-service:order-domain:order-application-service"))
    implementation(project(":common:common-domain"))

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Database
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.6.0")

}
