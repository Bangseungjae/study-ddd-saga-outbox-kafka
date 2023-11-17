package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.domain.event.payload.RestaurantOrderEventPayload;
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.kafka.order.avro.model.*;
import com.food.ordering.system.order.service.domain.dto.message.CustomerModel;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.domain.event.payload.PaymentOrderEventPayload;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OrderMessagingDataMapper {

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(
            PaymentOrderEventPayload paymentOrderEventPayload,
            debezium.payment.order_outbox.Value paymentResponseAvroModel
    ) {
        return new PaymentResponse(
                paymentResponseAvroModel.getId().toString(),
                paymentResponseAvroModel.getSagaId().toString(),
                paymentOrderEventPayload.getOrderId(),
                paymentOrderEventPayload.getPaymentId(),
                paymentOrderEventPayload.getCustomerId(),
                paymentOrderEventPayload.getPrice(),
                Instant.parse(paymentResponseAvroModel.getCreatedAt()),
                PaymentStatus.valueOf(paymentOrderEventPayload.getPaymentStatus()),
                paymentOrderEventPayload.getFailureMessages()
        );
    }

    public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(
            RestaurantOrderEventPayload restaurantOrderEventPayload,
            debezium.restaurant.order_outbox.Value restaurantApprovalResponseAvroModel
    ) {
        return new RestaurantApprovalResponse(
                restaurantApprovalResponseAvroModel.getId(),
                restaurantApprovalResponseAvroModel.getSagaId(),
                restaurantOrderEventPayload.getOrderId(),
                restaurantOrderEventPayload.getOrderId(),
                Instant.parse(restaurantApprovalResponseAvroModel.getCreatedAt()),
                OrderApprovalStatus.valueOf(restaurantOrderEventPayload.getOrderApprovalStatus()),
                restaurantOrderEventPayload.getFailureMessages()
        );
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
