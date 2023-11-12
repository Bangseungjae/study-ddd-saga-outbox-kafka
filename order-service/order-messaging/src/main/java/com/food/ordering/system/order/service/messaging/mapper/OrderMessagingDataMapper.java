package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.kafka.order.avro.model.*;
import com.food.ordering.system.order.service.domain.dto.message.CustomerModel;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingDataMapper {

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

    public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(
            String sagaId,
            OrderPaymentEventPayload orderPaymentEventPayload
    ) {
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(sagaId))
                .setCustomerId(UUID.fromString(orderPaymentEventPayload.getCustomerId()))
                .setOrderId(UUID.fromString(orderPaymentEventPayload.getOrderId()))
                .setPrice(orderPaymentEventPayload.getPrice())
                .setCreatedAt(orderPaymentEventPayload.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderApprovalEventToRestaurantApprovalRequestAvroModel(
            String sagaId,
            OrderApprovalEventPayload orderApprovalEventPayload
    ) {
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(sagaId))
                .setOrderId(UUID.fromString(orderApprovalEventPayload.getOrderId()))
                .setRestaurantId(UUID.fromString(orderApprovalEventPayload.getRestaurantId()))
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                .setProducts(orderApprovalEventPayload.getProducts().stream().map(orderApprovalEventProduct ->
                                Product.newBuilder()
                                        .setId(orderApprovalEventProduct.getId())
                                        .setQuantity(orderApprovalEventProduct.getQuantity())
                                        .build()
                        ).toList())
                .setPrice(orderApprovalEventPayload.getPrice())
                .setCreatedAt(orderApprovalEventPayload.getCreatedAt().toInstant())
                .build();
    }

    public CustomerModel customerAvroModelToCustomerModel(CustomerAvroModel customerAvroModel) {
        return new CustomerModel(
                customerAvroModel.getId().toString(),
                customerAvroModel.getUsername(),
                customerAvroModel.getFirstName(),
                customerAvroModel.getLastName()
        );
    }
}
