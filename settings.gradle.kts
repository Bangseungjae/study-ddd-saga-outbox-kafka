rootProject.name = "food-ordering-system"

// order
include("order-service")
include("order-service:order-dataaccess")
include("order-service:order-domain")
include("order-service:order-application")
include("order-service:order-domain:order-application-service")
include("order-service:order-domain:order-domain-core")
include("order-service:order-container")
include("order-service:order-application")
include("order-service:order-messaging")


// common
include("common")
include("common:common-domain")
include("common:common-application")
include(":common:common-dataaccess")
include(":common:common-messaging")

// infra
include(":infrastructure")
include(":infrastructure:kafka")
include(":infrastructure:saga")
include(":infrastructure:outbox")
include(":infrastructure:kafka:kafka-config-data")
include(":infrastructure:kafka:kafka-consumer")
include(":infrastructure:kafka:kafka-model")
include(":infrastructure:kafka:kafka-producer")

// customer
include("customer-service")
include("customer-service:customer-container")
include("customer-service:customer-application")
include("customer-service:customer-dataaccess")
include("customer-service:customer-messaging")
include("customer-service:customer-domain")
include("customer-service:customer-domain:customer-domain-core")
include("customer-service:customer-domain:customer-application-service")


// payment
include("payment-service")
include("payment-service:payment-container")
include("payment-service:payment-dataaccess")
include("payment-service:payment-messaging")
include("payment-service:payment-domain")
include("payment-service:payment-domain:payment-domain-core")
include("payment-service:payment-domain:payment-domain-service")

// restaurant
include("restaurant-service")
include("restaurant-service:restaurant-container")
include("restaurant-service:restaurant-dataaccess")
include("restaurant-service:restaurant-domain")
include("restaurant-service:restaurant-domain:restaurant-application-service")
include("restaurant-service:restaurant-domain:restaurant-domain-core")
include("restaurant-service:restaurant-messaging")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
