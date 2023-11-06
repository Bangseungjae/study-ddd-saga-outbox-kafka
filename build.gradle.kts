import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

    configurations {
        all {
            exclude("commons-logging:commons-logging")
            exclude("org.springframework.boot:spring-boot-starter-logging")
            exclude("org.slf4j:slf4j-reload4j")
            exclude(group = "org.slf4j", module = "slf4j-log4j12")
            exclude("org.slf4j:slf4j-api")
            exclude(group = "org.slf4j", module = "slf4j-simple")
            exclude("log4j:log4j")
            exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
//            exclude(group = "ch.qos.logback", module = "logback-classic")
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

val serializerVersion = "7.5.1"
val avroVersion = "1.11.3"

project(":infrastructure:kafka:kafka-config-data") {
    dependencies {
        implementation("org.springframework.kafka:spring-kafka")
        api("io.confluent:kafka-avro-serializer:$serializerVersion")
        api("org.apache.avro:avro:$avroVersion")
    }
}

project(":infrastructure:kafka:kafka-producer") {
    dependencies {
        api("io.confluent:kafka-avro-serializer:$serializerVersion")
        api("org.apache.avro:avro:$avroVersion")
    }
}

project(":infrastructure:kafka:kafka-consumer") {
    dependencies {
        api("io.confluent:kafka-avro-serializer:$serializerVersion")
        api("org.apache.avro:avro:$avroVersion")
    }
}

project(":infrastructure:kafka:kafka-model") {
    dependencies {
        api("org.springframework.kafka:spring-kafka")
        api("org.apache.avro:avro:${avroVersion}")
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
