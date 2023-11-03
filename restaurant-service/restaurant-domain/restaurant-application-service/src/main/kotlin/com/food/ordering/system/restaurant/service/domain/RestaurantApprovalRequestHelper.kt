package com.food.ordering.system.restaurant.service.domain

import com.food.ordering.system.domain.valueobject.OrderId
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest
import com.food.ordering.system.restaurant.service.domain.entity.Product
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException
import com.food.ordering.system.restaurant.service.domain.mapper.RestaurantDataMapper
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher
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
    private val orderApprovedMessagePublisher: OrderApprovedMessagePublisher,
    private val orderRejectedMessagePublisher: OrderRejectedMessagePublisher,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)


    @Transactional
    fun persistOrderApproval(restaurantApprovalRequest: RestaurantApprovalRequest): OrderApprovalEvent {
        logger.info("Processing restaurant approval for order id: ${restaurantApprovalRequest.orderId}")
        val failureMessages = mutableListOf<String>()
        val restaurant: Restaurant = findRestaurant(restaurantApprovalRequest)
        val orderApprovalEvent = restaurantDomainService.validateOrder(
            restaurant = restaurant,
            failureMessages = failureMessages,
            orderApprovedEventDomainEventPublisher = orderApprovedMessagePublisher,
            orderRejectedEventDomainEventPublisher = orderRejectedMessagePublisher,
        )
        orderApprovalRepository.save(restaurant.orderApproval)
        return orderApprovalEvent
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
}
