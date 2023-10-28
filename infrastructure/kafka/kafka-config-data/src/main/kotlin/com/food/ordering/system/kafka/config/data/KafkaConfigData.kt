package com.food.ordering.system.kafka.config.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class KafkaConfigData (
    @Value("$CONFIG_DATA.bootstrapServers")
    val bootstrapServers: String,
    @Value("$CONFIG_DATA.schemaRegistryUrlKey")
    val schemaRegistryUrlKey: String,
    @Value("$CONFIG_DATA.schemaRegistryUrl")
    val schemaRegistryUrl: String,
    @Value("$CONFIG_DATA.numOfPartitions")
    val numOfPartitions: Int,
    @Value("$CONFIG_DATA.replicationFactor")
    val replicationFactor: Short,
)

const val CONFIG_DATA = "kafka-config"
