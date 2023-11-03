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

// infra
include(":infrastructure")
include(":infrastructure:kafka")
include(":infrastructure:kafka:kafka-config-data")
include(":infrastructure:kafka:kafka-consumer")
include(":infrastructure:kafka:kafka-model")
include(":infrastructure:kafka:kafka-producer")

// customer
include("customer-service")


// payment
include("payment-service")
include("payment-service:payment-container")
include("payment-service:payment-dataaccess")
include("payment-service:payment-messaging")
include("payment-service:payment-domain")
include("payment-service:payment-domain:payment-domain-core")
include("payment-service:payment-domain:payment-domain-service")
