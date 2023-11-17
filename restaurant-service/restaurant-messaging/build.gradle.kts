
dependencies {
    api(project(":restaurant-service:restaurant-domain:restaurant-application-service"))
    api(project(":infrastructure:kafka:kafka-consumer"))
    api(project(":infrastructure:kafka:kafka-model"))
    api(project(":infrastructure:kafka:kafka-producer"))
    api(project(":common:common-domain"))
    api(project(":common:common-messaging"))
}
