import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.ir.backend.js.compile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.1.5" apply false
    id("io.spring.dependency-management") version "1.1.3" apply false
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22" apply false
    idea
}


idea {
    module.isDownloadJavadoc = true
    module.isDownloadSources = true
}


//tasks.named<BootJar>("bootJar") {
//    enabled = false
//}

//val bootJar: BootJar by tasks
//bootJar.enabled = false

tasks.named<Jar>("jar") {
    enabled = true
}
allprojects {
    group = "com.food.ordering.system"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://packages.confluent.io/maven")
        }
    }
}

subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        testImplementation("org.springframework.boot:spring-boot-starter-test")

    }
    configurations {
        all {
            exclude("commons-logging:commons-logging")
            exclude("ch.qos.logback:logback-classic")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

//dependencies {
//}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val serializer_version = "7.0.1"
val avro_version = "1.11.1"

project(":infrastructure:kafka:kafka-config-data") {
    dependencies {
        implementation("org.springframework.kafka:spring-kafka")
        implementation("io.confluent:kafka-avro-serializer:$serializer_version")
        implementation("org.apache.avro:avro:$avro_version")
    }
}

project(":infrastructure:kafka:kafka-producer") {
    dependencies {
        implementation("io.confluent:kafka-avro-serializer:$serializer_version")
    }
}

project(":infrastructure:kafka:kafka-consumer") {
    dependencies {
        implementation("io.confluent:kafka-avro-serializer:$serializer_version")
    }
}

project(":infrastructure:kafka:kafka-model") {
    dependencies {
        implementation("org.springframework.kafka:spring-kafka")
        implementation("io.confluent:kafka-avro-serializer:$serializer_version")
        implementation("org.apache.avro:avro:${avro_version}")
        implementation("org.apache.avro:avro-tools:${avro_version}")
        implementation("org.apache.avro:avro-maven-plugin:$avro_version")
    }
}


project(":order-service:order-domain:order-application-service") {
    dependencies {
        implementation(project(":order-service:order-domain:order-domain-core"))
        implementation(project(":common:common-domain"))
    }
}

project(":order-service:order-domain:order-domain-core") {
    dependencies {
        implementation(project(":common:common-domain"))
    }
}

project(":order-service:order-dataaccess") {
    dependencies {
        implementation(project(":common:common-domain"))
        implementation(project(":order-service:order-domain:order-application-service"))
        implementation(project(":order-service:order-domain:order-domain-core"))
    }
}
