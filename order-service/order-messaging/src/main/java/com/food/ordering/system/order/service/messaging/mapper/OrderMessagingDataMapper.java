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

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString("68f173a1-a28d-42ca-b720-72497bbea664"))
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(""))
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString("68f173a1-a28d-42ca-b720-72497bbea664"))
                .setOrderId(order.getId().getValue())
                .setRestaurantId(order.getRestaurantId().getId())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setProducts(order.getItems().stream().map(orderItem ->
                        new Product(orderItem.getProduct().getId().getValue().toString(), orderItem.getQuantity()))
                        .toList())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        return new PaymentResponse(
                paymentResponseAvroModel.getId().toString(),
                paymentResponseAvroModel.getSagaId().toString(),
                paymentResponseAvroModel.getOrderId().toString(),
                paymentResponseAvroModel.getPaymentId().toString(),
                paymentResponseAvroModel.getCustomerId().toString(),
                paymentResponseAvroModel.getPrice(),
                paymentResponseAvroModel.getCreatedAt(),
                PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()),
                paymentResponseAvroModel.getFailureMessages()
        );
    }

    public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(RestaurantApprovalResponseAvroModel model) {
        return new RestaurantApprovalResponse(
                model.getId().toString(),
                model.getSagaId().toString(),
                model.getOrderId().toString(),
                model.getRestaurantId().toString(),
                model.getCreatedAt(),
                OrderApprovalStatus.valueOf(model.getOrderApprovalStatus().name()),
                model.getFailureMessages()
        );
    }
}
