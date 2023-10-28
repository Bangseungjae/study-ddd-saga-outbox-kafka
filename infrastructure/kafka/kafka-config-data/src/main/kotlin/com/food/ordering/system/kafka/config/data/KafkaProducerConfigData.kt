package com.food.ordering.system.kafka.config.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class KafkaProducerConfigData(
    @Value("$PRODUCER_CONFIG.keySerializerClass")
    val keySerializerClass: String,

    @Value("$PRODUCER_CONFIG.valueSerializerClass")
    val valueSerializerClass: String,

    @Value("$PRODUCER_CONFIG.compressionType")
    val compressionType: String,

    @Value("$PRODUCER_CONFIG.acks")
    val acks: String,

    @Value("$PRODUCER_CONFIG.batchSize")
    val batchSize: Int,

    @Value("$PRODUCER_CONFIG.batchSizeBoostFactor")
    val batchSizeBoostFactor: Int,

    @Value("$PRODUCER_CONFIG.lingerMs")
    val lingerMs: Int,

    @Value("$PRODUCER_CONFIG.requestTimeoutMs")
    val requestTimeoutMs: Int,

    @Value("$PRODUCER_CONFIG.retryCount")
    val retryCount: Int,
)

const val PRODUCER_CONFIG = "kafka-producer-config"
