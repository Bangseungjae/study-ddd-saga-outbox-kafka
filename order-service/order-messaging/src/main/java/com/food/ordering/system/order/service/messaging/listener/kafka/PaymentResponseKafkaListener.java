package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.config.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.messaging.DebeziumOp;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.domain.event.payload.PaymentOrderEventPayload;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import debezium.payment.order_outbox.Envelope;
import debezium.payment.order_outbox.Value;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<Envelope> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaMessageHelper kafkaMessageHelper;

    public PaymentResponseKafkaListener(
            PaymentResponseMessageListener paymentResponseMessageListener,
            OrderMessagingDataMapper orderMessagingDataMapper,
            KafkaMessageHelper kafkaMessageHelper
    ) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}")
    public void receive(
            @Payload @NotNull List<? extends Envelope> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) @NotNull List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) @NotNull List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) @NotNull List<Long> offsets
    ) {
        logger.info("{} number of payment responses received",
                messages.stream().filter(message ->
                                message.getBefore() == null && DebeziumOp.CREATE.getValue().equals(message.getOp()))
                        .toList().size());

        messages.forEach(avroModel -> {
            if (avroModel.getBefore() == null && DebeziumOp.CREATE.getValue().equals(avroModel.getOp())) {
                logger.info("Incoming message in PaymentResponseKafkaListener: {}", avroModel);
                Value paymentResponseAvroModel = avroModel.getAfter();
                PaymentOrderEventPayload paymentOrderEventPayload = kafkaMessageHelper.getOrderEventPayload(
                        paymentResponseAvroModel.getPayload(),
                        PaymentOrderEventPayload.class
                );
                try {
                    if (PaymentStatus.COMPLETED.name().equals(paymentOrderEventPayload.getPaymentStatus())) {
                        logger.info("Processing successful payment for order id: {}",
                                paymentOrderEventPayload.getOrderId());
                        paymentResponseMessageListener.paymentCompleted(
                                orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(
                                        paymentOrderEventPayload,
                                        paymentResponseAvroModel
                                )
                        );
                    } else if (PaymentStatus.CANCELLED.name().equals(paymentOrderEventPayload.getPaymentStatus()) ||
                            PaymentStatus.FAILED.name().equals(paymentResponseAvroModel.getPaymentStatus())) {
                        logger.info("Processing unsuccessful payment for order id: {}", paymentOrderEventPayload.getOrderId());
                        paymentResponseMessageListener.paymentCancelled(
                                orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(
                                        paymentOrderEventPayload,
                                        paymentResponseAvroModel
                                )
                        );
                    }
                } catch (OptimisticLockingFailureException e) {
                    /*
                     * NO-OP for optimistic lock. This means another thread finished the work, do not throw error
                     * to prevent reading the data from kafka again!
                     */
                    logger.error("Caught optimistic locking exception in PaymentResponseKafkaListener for order id: {}",
                            paymentOrderEventPayload.getOrderId());
                } catch (OrderNotFoundException e) {
                    // NO-OP for OrderNotFoundException
                    logger.error("No order found for id: {}", paymentOrderEventPayload.getOrderId());
                } catch (DataAccessException e) {
                    SQLException sqlException = (SQLException) e.getRootCause();
                    if (sqlException != null && sqlException.getSQLState() != null) {
                        logger.error("Caught unique constraint exception with sql state: {} in " +
                                        "PaymentResponseKafkaListener for order id: {}",
                                sqlException.getSQLState(),
                                paymentOrderEventPayload.getOrderId()
                        );
                    }
                }
            }
        });
    }
}
