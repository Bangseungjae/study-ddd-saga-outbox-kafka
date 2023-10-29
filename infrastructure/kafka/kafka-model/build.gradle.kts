import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask
import com.github.davidmc24.gradle.plugin.avro.GenerateAvroProtocolTask
import org.jetbrains.kotlin.ir.backend.js.compile
import org.jetbrains.kotlin.js.backend.ast.JsEmpty.source

plugins {
    id("com.github.davidmc24.gradle.plugin.avro") version "1.0.0"
}




val avroVersion = "1.11.0"



repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.apache.avro:avro:${avroVersion}")
    implementation("org.apache.avro:avro-tools:${avroVersion}")
    // https://mvnrepository.com/artifact/com.github.avro-kotlin.avro4k/avro4k-core
//    runtimeOnly("com.github.avro-kotlin.avro4k:avro4k-core:1.9.0")


    //avro-serializer
    implementation("io.confluent:kafka-avro-serializer:5.5.1")
}


val generateAvro = tasks.register<GenerateAvroJavaTask>("generateAvro") {
    source("src/main/avro")
    setOutputDir(file("src/main/java"))
}


//tasks.named("compileJava").configure {
//    source(generateAvro)
//}

//val generateAvroProtocol = tasks.register<GenerateAvroProtocolTask>("generateAvroV2") {
//    source("src/main/avro")
//    setOutputDir(file("src/main/kotlin/com/food/ordering/system/kafka/ordering/avro/model"))
//}


tasks.named("compileJava").configure {
    source(generateAvro)
}
