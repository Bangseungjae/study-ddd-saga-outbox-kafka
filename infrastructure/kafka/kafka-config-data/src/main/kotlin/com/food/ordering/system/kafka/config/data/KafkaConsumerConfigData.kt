package com.food.ordering.system.kafka.config.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class KafkaConsumerConfigData (
    @Value("\${$CONSUMER_CONFIG.key-deserializer}")
    val keyDeserializer: String,
    @Value("\${$CONSUMER_CONFIG.value-deserializer}")
    val valueDeserializer: String,
    @Value("\${$CONSUMER_CONFIG.auto-offset-reset}")
    val autoOffsetReset: String,
    @Value("\${$CONSUMER_CONFIG.specific-avro-reader-key}")
    val specificAvroReaderKey: String,
    @Value("\${$CONSUMER_CONFIG.specific-avro-reader}")
    val specificAvroReader: String,
    @Value("\${$CONSUMER_CONFIG.batch-listener}")
    val batchListener: Boolean,
    @Value("\${$CONSUMER_CONFIG.auto-startup}")
    val autoStartup: Boolean,
    @Value("\${$CONSUMER_CONFIG.concurrency-level}")
    val concurrencyLevel: Int,
    @Value("\${$CONSUMER_CONFIG.session-timeout-ms}")
    val sessionTimeoutMs: Int,
    @Value("\${$CONSUMER_CONFIG.heartbeat-interval-ms}")
    val heartbeatIntervalMs: Int,
    @Value("\${$CONSUMER_CONFIG.max-poll-interval-ms}")
    val maxPollIntervalMs: Int,
    @Value("\${$CONSUMER_CONFIG.poll-timeout-ms}")
    val pollTimeoutMs: Long,
    @Value("\${$CONSUMER_CONFIG.max-poll-records}")
    val maxPollRecords: Int,
    @Value("\${$CONSUMER_CONFIG.max-partition-fetch-bytes-default}")
    val maxPartitionFetchBytesDefault: Int,
    @Value("\${$CONSUMER_CONFIG.max-partition-fetch-bytes-boost-factor}")
    val maxPartitionFetchBytesBoostFactor: Int,
)
const val CONSUMER_CONFIG: String = "kafka-consumer-config"
