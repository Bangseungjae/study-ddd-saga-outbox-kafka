package com.food.ordering.system.restaurant.service.domain

import com.food.ordering.system.domain.valueobject.OrderId
import com.food.ordering.system.outbox.OutboxStatus
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest
import com.food.ordering.system.restaurant.service.domain.entity.Product
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException
import com.food.ordering.system.restaurant.service.domain.mapper.RestaurantDataMapper
import com.food.ordering.system.restaurant.service.domain.outbox.scheduler.OrderOutboxHelper
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.RestaurantRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class RestaurantApprovalRequestHelper(
    private val restaurantDomainService: RestaurantDomainService,
    private val restaurantDataMapper: RestaurantDataMapper,
    private val restaurantRepository: RestaurantRepository,
    private val orderApprovalRepository: OrderApprovalRepository,
    private val orderOutboxHelper: OrderOutboxHelper,
    private val restaurantApprovalResponseMessagePublisher: RestaurantApprovalResponseMessagePublisher,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)


    @Transactional
    fun persistOrderApproval(restaurantApprovalRequest: RestaurantApprovalRequest) {

        if (publishIfOutboxMessageProcessed(restaurantApprovalRequest)) {
            logger.info("An outbox message with saga id: ${restaurantApprovalRequest.id} already saved to database!")
        }

        logger.info("Processing restaurant approval for order id: ${restaurantApprovalRequest.orderId}")
        val failureMessages = mutableListOf<String>()
        val restaurant: Restaurant = findRestaurant(restaurantApprovalRequest)
        val orderApprovalEvent = restaurantDomainService.validateOrder(
            restaurant = restaurant,
            failureMessages = failureMessages,
        )
        orderApprovalRepository.save(restaurant.orderApproval)

        orderOutboxHelper.saveOrderOutboxMessage(
            orderEventPayload = restaurantDataMapper.orderApprovalEventToEventPayload(orderApprovalEvent),
            approvalStatus = orderApprovalEvent.orderApproval.orderApprovalStatus,
            outboxStatus = OutboxStatus.STARTED,
            sagaId = UUID.fromString(restaurantApprovalRequest.sagaId),
        )
    }

    private fun findRestaurant(restaurantApprovalRequest: RestaurantApprovalRequest): Restaurant {
        val restaurant = restaurantDataMapper
            .restaurantApprovalRequestToRestaurant(restaurantApprovalRequest)
        val restaurantEntity = restaurantRepository.findRestaurantInformation(restaurant)
            ?: run {
                logger.error("Restaurant with id: ${restaurant.id.value}, not found!")
                throw RestaurantNotFoundException("Restaurant with id: ${restaurant.id.value}, not found!")
            }

        restaurant.active = true
        restaurant.orderDetail.products.forEach{product: Product ->
            restaurantEntity.orderDetail.products.forEach{p ->
                if (p.id == product.id) {
                    product.updateWithConfirmedNamePriceAndAvailability(
                        name = p.name,
                        price = p.price,
                        available = p.available,
                    )
                }
            }
        }
        restaurant.orderDetail.id = OrderId(UUID.fromString(restaurantApprovalRequest.orderId))
        return restaurant
    }

    private fun publishIfOutboxMessageProcessed(restaurantApprovalRequest: RestaurantApprovalRequest): Boolean {
        orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(
            sagaId = UUID.fromString(restaurantApprovalRequest.sagaId),
            outboxStatus = OutboxStatus.COMPLETED
        )?.let {
            restaurantApprovalResponseMessagePublisher.publish(
                orderOutboxMessage = it,
                outboxCallback = orderOutboxHelper::updateOutboxMessage
            )
            return true
        } ?: return false
    }
}
