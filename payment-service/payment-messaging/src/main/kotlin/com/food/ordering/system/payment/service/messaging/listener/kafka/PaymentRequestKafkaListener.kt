package com.food.ordering.system.payment.service.messaging.listener.kafka

import com.food.ordering.system.domain.event.payload.OrderPaymentEventPayload
import com.food.ordering.system.domain.valueobject.PaymentOrderStatus
import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper
import com.food.ordering.system.kafka.consumer.KafkaSingleItemConsumer
import com.food.ordering.system.messaging.DebeziumOp
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException
import com.food.ordering.system.payment.service.domain.exception.PaymentNotFoundException
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener
import com.food.ordering.system.payment.service.messaging.mapper.toPaymentRequest
import debezium.order.payment_outbox.Envelope
import org.postgresql.util.PSQLState
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.sql.SQLException

@Component
class PaymentRequestKafkaListener(
    private val paymentRequestMessageListener: PaymentRequestMessageListener,
    private val kafkaMessageHelper: KafkaMessageHelper,
) : KafkaSingleItemConsumer<Envelope> {

    private val logger = LoggerFactory.getLogger(this::class.java)

//    @KafkaListener(
//        id = "\${kafka-consumer-config.payment-consumer-group-id}",
//        topics = ["\${payment-service.payment-request-topic-name}"],
//    )
//    override fun receive(
//        @Payload messages: List<PaymentRequestAvroModel>,
//        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
//        @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
//        @Header(KafkaHeaders.OFFSET) offsets: List<Long>,
//    ) {
//        logger.info(
//            "${messages.size} number of payment requests received with keys: $keys, " +
//                    "partitions: $partitions and offsets: $offsets"
//        )
//
//        logger.info("kafka listener messages size: ${messages.size}")
//        messages.forEach { paymentRequestAvroModel ->
//            try {
//                logger.info("Customer id: ${paymentRequestAvroModel.orderId}")
//                if (PaymentOrderStatus.PENDING == paymentRequestAvroModel.paymentOrderStatus) {
//                    logger.info("Processing payment for order id: ${paymentRequestAvroModel.orderId}")
//                    paymentRequestMessageListener.completePayment(
//                        paymentRequestAvroModel.toPaymentRequest()
//                    )
//                } else if (PaymentOrderStatus.CANCELLED == paymentRequestAvroModel.paymentOrderStatus) {
//                    logger.info("Cancelling payment for order id: ${paymentRequestAvroModel.orderId}")
//                    paymentRequestMessageListener.cancelPayment(
//                        paymentRequestAvroModel.toPaymentRequest()
//                    )
//                }
//            } catch (e: DataAccessException) {
//                val sqlException = e.rootCause as SQLException
//                if (sqlException.sqlState == null) {
//                    throw PaymentApplicationServiceException(
//                        "Throwing DataAccessException in " +
//                                "PaymentRequestKafkaListener: ${e.message}"
//                    )
//                }
//                when (sqlException.sqlState) {
//                    PSQLState.UNIQUE_VIOLATION.state -> {
//                        //NO-OP unique constraint exception
//                        logger.error(
//                            "Caught unique constraint exception with sql state: ${sqlException.sqlState} " +
//                                    "in PaymentRequestKafkaListener for order id: ${paymentRequestAvroModel.orderId}"
//                        )
//                    }
//
//                    else -> {
//                        throw PaymentApplicationServiceException(
//                            "Throwing DataAccessException in " +
//                                    "PaymentRequestKafkaListener: ${e.message}"
//                        )
//                    }
//                }
//            } catch (e: PaymentNotFoundException) {
//                //NO-OP for PaymentNotFoundException
//                logger.error("No payment found for order id: ${paymentRequestAvroModel.orderId}")
//            }
//        }
//    }

    @KafkaListener(
        id = "\${kafka-consumer-config.payment-consumer-group-id}",
        topics = ["\${payment-service.payment-request-topic-name}"],
    )
    override fun receive(
        @Payload message: Envelope,
        @Header(KafkaHeaders.RECEIVED_KEY) key: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset: Long,
    ) {
        if (message.before == null && DebeziumOp.CREATE.value == message.op) {
            logger.info(
                "Incoming Message in PaymentRequestKafkaListener $message with key: $key, " +
                        "partition: $partition and offset: $offset"
            )
            val paymentRequestAvroModel = message.after
            val orderPaymentEventPayload = kafkaMessageHelper.getOrderEventPayload(
                paymentRequestAvroModel.payload,
                OrderPaymentEventPayload::class.java
            )
            try {
                if (PaymentOrderStatus.PENDING.name == orderPaymentEventPayload.paymentOrderStatus) {
                    logger.info("Processing for order id: ${orderPaymentEventPayload.orderId}")
                    paymentRequestMessageListener.completePayment(
                        orderPaymentEventPayload.toPaymentRequest(paymentRequestAvroModel)
                    )
                } else if (PaymentOrderStatus.CANCELLED.name == orderPaymentEventPayload.paymentOrderStatus) {
                    logger.info("Cancelling payment for order id: ${orderPaymentEventPayload.orderId}")
                    paymentRequestMessageListener.cancelPayment(
                        orderPaymentEventPayload.toPaymentRequest(paymentRequestAvroModel)
                    )
                }
            } catch (e: DataAccessException) {
                val sqlException = e.rootCause as SQLException
                if (sqlException.sqlState != null && PSQLState.UNIQUE_VIOLATION.state == sqlException.sqlState) {
                    //NO-OP for unique constraint exception
                    logger.error(
                        "Caught unique constraint exception with sql state: ${sqlException.sqlState} " +
                                "in PaymentRequestKafkaListener for order id: ${orderPaymentEventPayload.orderId}"
                    )
                } else {
                    throw PaymentApplicationServiceException(
                        "Throwing DataAccessException in " +
                                "PaymentRequestKafkaListener: ${e.message}"
                    )
                }
            } catch (e: PaymentNotFoundException) {
                //NO-OP for PaymentNotFoundException
                logger.error("No payment found for order id: ${orderPaymentEventPayload.orderId}")
            }
        }
    }
}
