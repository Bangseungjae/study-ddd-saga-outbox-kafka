
dependencies {
    api(project(":restaurant-service:restaurant-domain:restaurant-domain-core"))
    api(project(":infrastructure:outbox"))
    api(project(":infrastructure:saga"))
    api(project(":common:common-domain"))
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-json")
}
