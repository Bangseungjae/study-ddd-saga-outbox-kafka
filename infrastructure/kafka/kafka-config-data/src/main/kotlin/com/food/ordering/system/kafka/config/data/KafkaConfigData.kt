package com.food.ordering.system.kafka.config.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class KafkaConfigData (
    @Value("\${$CONFIG_DATA.bootstrap-servers}")
    val bootstrapServers: String,
    @Value("\${$CONFIG_DATA.schema-registry-url-key}")
    val schemaRegistryUrlKey: String,
    @Value("\${$CONFIG_DATA.schema-registry-url}")
    val schemaRegistryUrl: String,
    @Value("\${$CONFIG_DATA.num-of-partitions}")
    val numOfPartitions: Int,
    @Value("\${$CONFIG_DATA.replication-factor}")
    val replicationFactor: Short,
)

const val CONFIG_DATA = "kafka-config"
