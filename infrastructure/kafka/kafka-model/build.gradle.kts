import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask
import org.jetbrains.kotlin.js.backend.ast.JsEmpty.source
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.0"
}

val generateAvro = tasks.register<GenerateAvroJavaTask>("generateAvro") {
    source("src/main/avro")
    setOutputDir(file("src/main/java"))
}


tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}


tasks.named("compileJava").configure {
    source(generateAvro)
}
