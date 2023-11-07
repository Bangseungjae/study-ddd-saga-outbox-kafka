package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public PaymentResponseKafkaListener(
            PaymentResponseMessageListener paymentResponseMessageListener,
            OrderMessagingDataMapper orderMessagingDataMapper
    ) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}")
    public void receive(
            @Payload @NotNull List<? extends PaymentResponseAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) @NotNull List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) @NotNull List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) @NotNull List<Long> offsets
    ) {
        logger.info("{} number of payment responses received with keys: {}, partitions: {}, and offsets: {}",
                messages.size(),
                keys,
                partitions,
                offsets);

        messages.forEach(paymentResponseAvroModel -> {
            try {
                if (PaymentStatus.COMPLETED == paymentResponseAvroModel.getPaymentStatus()) {
                    logger.info("Processing successful payment for order id: {}",
                            paymentResponseAvroModel.getOrderId());
                    paymentResponseMessageListener.paymentCompleted(
                            orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel)
                    );
                } else if (PaymentStatus.CANCELLED == paymentResponseAvroModel.getPaymentStatus() ||
                        PaymentStatus.FAILED == paymentResponseAvroModel.getPaymentStatus()) {
                    logger.info("Processing unsuccessful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                    paymentResponseMessageListener.paymentCancelled(
                            orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel)
                    );
                }
            } catch (OptimisticLockingFailureException e) {
                /*
                 * NO-OP for optimistic lock. This means another thread finished the work, do not throw error
                 * to prevent reading the data from kafka again!
                 */
                logger.error("Caught optimistic locking exception in PaymentResponseKafkaListener for order id: {}",
                        paymentResponseAvroModel.getOrderId());
            } catch (OrderNotFoundException e) {
                // NO-OP for OrderNotFoundException
                logger.error("No order found for id: {}", paymentResponseAvroModel.getOrderId());
            }
        });
    }
}
