package com.food.ordering.system.restaurant.service.domain

import com.food.ordering.system.domain.KOREA_DATE_TIME
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.time.ZonedDateTime

class RestaurantDomainServiceImpl : RestaurantDomainService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun validateOrder(
        restaurant: Restaurant,
        failureMessages: MutableList<String>,
        orderApprovedEventDomainEventPublisher: DomainEventPublisher<OrderApprovedEvent>,
        orderRejectedEventDomainEventPublisher: DomainEventPublisher<OrderRejectedEvent>,
    ): OrderApprovalEvent {
        logger.info("Validating order with id: ${restaurant.orderDetail.id.value}")
        restaurant.validateOrder(failureMessages)

        if (failureMessages.isEmpty()) {
            logger.info("Order is approved for order id: ${restaurant.orderDetail.id.value}")
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED)
            return OrderApprovedEvent(
                orderApproval = restaurant.orderApproval,
                restaurantId = restaurant.id,
                failureMessages = failureMessages,
                createdAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME)),
                orderApprovedEventDomainEventPublisher = orderApprovedEventDomainEventPublisher,
            )
        } else {
            logger.info("Order is reject for order id: ${restaurant.orderDetail.id.value}")
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED)
            return OrderRejectedEvent(
                orderApproval = restaurant.orderApproval,
                restaurantId = restaurant.id,
                failureMessages = failureMessages,
                createdAt = ZonedDateTime.now(ZoneId.of(KOREA_DATE_TIME)),
                orderRejectedEventDomainEventPublisher = orderRejectedEventDomainEventPublisher,
            )
        }
    }
}
