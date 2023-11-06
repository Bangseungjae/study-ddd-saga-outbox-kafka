
dependencies {
    implementation(project(":restaurant-service:restaurant-domain:restaurant-domain-core"))
    implementation(project(":restaurant-service:restaurant-domain:restaurant-application-service"))
    implementation(project(":restaurant-service:restaurant-dataaccess"))
    implementation(project(":restaurant-service:restaurant-messaging"))

//    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

}
