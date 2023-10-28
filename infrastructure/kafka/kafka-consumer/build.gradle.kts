


dependencies {
    implementation(project(":order-service:order-domain:order-domain-core"))
    implementation(project(":infrastructure:kafka:kafka-config-data"))
    implementation(project(":infrastructure:kafka:kafka-model"))
    implementation(project(":common:common-domain"))
//    implementation("io.confluent:kafka-avro-serializer")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka")
}
