package com.food.ordering.system.kafka.config.producer.service

import org.apache.avro.specific.SpecificRecordBase
import org.springframework.kafka.support.SendResult
import java.io.Serializable
import java.util.concurrent.CompletableFuture

interface KafkaProducer <K: Serializable, V: SpecificRecordBase> {
    fun send(
        topicName: String,
        key: K,
        message: V,
        callback: CompletableFuture<SendResult<K, V>>,
    )
}
