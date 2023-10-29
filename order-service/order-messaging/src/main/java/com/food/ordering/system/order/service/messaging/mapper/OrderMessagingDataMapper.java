package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.kafka.order.avro.model.*;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {
    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();
        return new PaymentRequestAvroModel(
                UUID.randomUUID().toString(),
                "",
                order.getCustomerId().getValue().toString(),
                order.getId().getValue().toString(),
                order.getPrice().getAmount(),
                orderCancelledEvent.getCreatedAt().toInstant(),
                PaymentOrderStatus.CANCELLED
        );
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return new RestaurantApprovalRequestAvroModel(
                UUID.randomUUID().toString(),
                "",
                order.getRestaurantId().getValue().toString(),
                order.getId().getValue().toString(),
                RestaurantOrderStatus.PAID,
                order.getItems().stream().map(
                        orderItem -> new Product(
                                orderItem.getId().getValue().toString(),
                                orderItem.getQuantity())
                ).collect(Collectors.toList()),
                order.getPrice().getAmount(),
                orderPaidEvent.getCreatedAt().toInstant()
        );
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        return new PaymentResponse(
                paymentResponseAvroModel.getId(),
                paymentResponseAvroModel.getSagaId(),
                paymentResponseAvroModel.getOrderId(),
                paymentResponseAvroModel.getPaymentId(),
                paymentResponseAvroModel.getCustomerId(),
                paymentResponseAvroModel.getPrice(),
                paymentResponseAvroModel.getCreatedAt(),
                PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()),
                paymentResponseAvroModel.getFailureMessages()
        );
    }

    public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(RestaurantApprovalResponseAvroModel model) {
        return new RestaurantApprovalResponse(
                model.getId(),
                model.getSagaId(),
                model.getOrderId(),
                model.getRestaurantId(),
                model.getCreatedAt(),
                OrderApprovalStatus.valueOf(model.getOrderApprovalStatus().name()),
                model.getFailureMessages()
        );
    }
}
