package com.food.ordering.system.kafka.config.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class KafkaConsumerConfigData (
    @Value("$CONSUMER_CONFIG.keyDeserializer")
    val keyDeserializer: String,
    @Value("$CONSUMER_CONFIG.valueDeserializer")
    val valueDeserializer: String,
    @Value("$CONSUMER_CONFIG.autoOffsetReset")
    val autoOffsetReset: String,
    @Value("$CONSUMER_CONFIG.specificAvroReaderKey")
    val specificAvroReaderKey: String,
    @Value("$CONSUMER_CONFIG.specificAvroReader")
    val specificAvroReader: String,
    @Value("$CONSUMER_CONFIG.batchListener")
    val batchListener: Boolean,
    @Value("$CONSUMER_CONFIG.autoStartup")
    val autoStartup: Boolean,
    @Value("$CONSUMER_CONFIG.concurrencyLevel")
    val concurrencyLevel: Int,
    @Value("$CONSUMER_CONFIG.sessionTimeoutMs")
    val sessionTimeoutMs: Int,
    @Value("$CONSUMER_CONFIG.heartbeatIntervalMs")
    val heartbeatIntervalMs: Int,
    @Value("$CONSUMER_CONFIG.maxPollIntervalMs")
    val maxPollIntervalMs: Int,
    @Value("$CONSUMER_CONFIG.pollTimeoutMs")
    val pollTimeoutMs: Long,
    @Value("$CONSUMER_CONFIG.maxPollRecords")
    val maxPollRecords: Int,
    @Value("$CONSUMER_CONFIG.maxPartitionFetchBytesDefault")
    val maxPartitionFetchBytesDefault: Int,
    @Value("$CONSUMER_CONFIG.maxPartitionFetchBytesBoostFactor")
    val maxPartitionFetchBytesBoostFactor: Int,
)
const val CONSUMER_CONFIG: String = "kafka-consumer-config"
