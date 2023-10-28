dependencies {
    implementation(project(":order-service:order-domain:order-application-service"))
    implementation(project(":infrastructure:kafka:kafka-consumer"))
    implementation(project(":infrastructure:kafka:kafka-producer"))
    implementation(project(":infrastructure:kafka:kafka-model"))
    implementation(project(":common:common-application"))
    implementation(project(":common:common-domain"))
    implementation(project(":order-service:order-domain:order-domain-core"))
}
