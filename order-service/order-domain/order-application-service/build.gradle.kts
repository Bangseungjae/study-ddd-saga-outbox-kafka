import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":order-service:order-domain:order-domain-core"))
    implementation(project(":common:common-domain"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework:spring-tx")

//    implementation("org.mockito:mockito-core")
    implementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
}

//tasks.named<BootJar>("bootJar") {
//    enabled = false
//}
//
//tasks.named<Jar>("Jar") {
//    enabled = true
//}
