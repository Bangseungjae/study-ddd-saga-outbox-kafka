package com.food.ordering.system.kafka.config.producer

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import com.food.ordering.system.outbox.OutboxStatus
import org.slf4j.LoggerFactory
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.function.BiConsumer

@Component
class KafkaMessageHelper(
    private val objectMapper: ObjectMapper,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun <T> getOrderEventPayload(
        payload: String,
        outputType: Class<T>,
    ): T {
        try {
            return objectMapper.readValue(payload, outputType)
        } catch (e: JsonProcessingException) {
            logger.error("Could not read ${outputType.name}", e)
            throw OrderDomainException("Could not read ${outputType.name}")
        }
    }

    fun <T, U> getKafkaCallback(
        responseTopicName: String,
        avroModel: T,
        outboxMessage: U,
        outboxCallback: BiConsumer<U, OutboxStatus>,
        orderId: String,
        avroModelName: String,
    ): BiConsumer<SendResult<String, T>, Throwable?> {
        return BiConsumer { result, ex ->
            if (ex == null) {
                val metadata = result.recordMetadata
                logger.info("Received successful response from kafka for order id: $orderId Topic: ${metadata.topic()} " +
                        "Partition: ${metadata.partition()} Offset: ${metadata.offset()} Timestamp: ${metadata.timestamp()}")

                outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED)
            } else {
                logger.error("Error while sending $avroModelName with message: ${avroModel.toString()} and outbox type: " +
                        "${outboxMessage!!::class.java.name} to topic: $responseTopicName"
                )
                outboxCallback.accept(outboxMessage, OutboxStatus.FAILED)
            }

        }
    }

}
