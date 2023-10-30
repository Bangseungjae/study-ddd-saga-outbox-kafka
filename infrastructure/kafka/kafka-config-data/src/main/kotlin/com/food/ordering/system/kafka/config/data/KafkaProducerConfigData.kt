package com.food.ordering.system.kafka.config.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class KafkaProducerConfigData(
    @Value("\${$PRODUCER_CONFIG.key-serializer-class}")
    val keySerializerClass: String,

    @Value("\${$PRODUCER_CONFIG.value-serializer-class}")
    val valueSerializerClass: String,

    @Value("\${$PRODUCER_CONFIG.compression-type}")
    val compressionType: String,

    @Value("\${$PRODUCER_CONFIG.acks}")
    val acks: String,

    @Value("\${$PRODUCER_CONFIG.batch-size}")
    val batchSize: Int,

    @Value("\${$PRODUCER_CONFIG.batch-size-boost-factor}")
    val batchSizeBoostFactor: Int,

    @Value("\${$PRODUCER_CONFIG.linger-ms}")
    val lingerMs: Int,

    @Value("\${$PRODUCER_CONFIG.request-timeout-ms}")
    val requestTimeoutMs: Int,

    @Value("\${$PRODUCER_CONFIG.retry-count}")
    val retryCount: Int,
)

const val PRODUCER_CONFIG = "kafka-producer-config"
