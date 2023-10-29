package com.food.ordering.system.kafka.config.producer.service.impl

import com.food.ordering.system.kafka.config.producer.service.KafkaProducer
import jakarta.annotation.PreDestroy
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.common.KafkaException
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFutureCallback
import java.io.Serializable
import java.util.concurrent.CompletableFuture

@Component
class KafkaProducerImpl <K: Serializable, V: SpecificRecordBase>(
    private val kafkaTemplate: KafkaTemplate<K, V>
) : KafkaProducer<K, V>{

    private val logger = LoggerFactory.getLogger(this::class.java)


    @PreDestroy
    fun close() {
        if (kafkaTemplate != null) {
            logger.info("Closing kafka producer")
            kafkaTemplate.destroy()
        }
    }

    override fun send(topicName: String, key: K, message: V, callback: ListenableFutureCallback<SendResult<K, V>>) {
        logger.info("Sending message=$message to topic=$topicName")

        try {
            val kafkaResultFuture: CompletableFuture<SendResult<K, V>> = kafkaTemplate.send(topicName, key, message)
//            kafkaResultFuture.completeAsync { callback. }
        } catch (e: KafkaException) {
            logger.info("Error on kafka producer with key: $key message: $message and exception: ${e.message}")
            throw KafkaException("Error on kafka producer with key: $key message: $message")
        }
    }
}
