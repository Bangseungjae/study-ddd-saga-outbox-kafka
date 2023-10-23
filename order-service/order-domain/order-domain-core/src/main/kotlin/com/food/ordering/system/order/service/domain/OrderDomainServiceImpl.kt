package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.entity.Restaurant
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.time.ZonedDateTime

const val ZONE_KO = "Asia/Seoul"

class OrderDomainServiceImpl : OrderDomainService {

    val logger = LoggerFactory.getLogger(this.javaClass)

    override fun validateAndInitiateOrder(order: Order, restaurant: Restaurant): OrderCreatedEvent {
        validateRestaurant(restaurant)
        setOrderProductInformation(order, restaurant)
        order.validateOrder()
        logger.info("Order with id: ${order.id.id} is initiated}")
        return OrderCreatedEvent(
            order = order,
            createdAt = ZonedDateTime.now(ZoneId.of(ZONE_KO))
        )

    }

    private fun setOrderProductInformation(order: Order, restaurant: Restaurant) {
        order.items.forEach { orderItem ->
            restaurant.products.forEach { restaurantProduct ->
                val currentProduct = orderItem.product
                if (currentProduct == restaurantProduct) {
                    currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.name, restaurantProduct.price)
                }
            }
        }
    }

    private fun validateRestaurant(restaurant: Restaurant) {
        if (!restaurant.active) {
            throw OrderDomainException("Restaurant with id ${restaurant.id.id} is currently not active!")
        }
    }

    override fun payOrder(order: Order): OrderPaidEvent {
        order.pay()
        logger.info("Order with id: ${order.id.id} is paid")
        return OrderPaidEvent(
            order = order,
            createdAt = ZonedDateTime.now(ZoneId.of(ZONE_KO))
        )
    }

    override fun approveOrder(order: Order) {
        order.approve()
        logger.info("Order with id: ${order.id.id} is approved")
    }

    override fun cancelOrderPayment(order: Order, failureMessage: List<String>): OrderCancelledEvent {
        order.initCancel(failureMessage)
        logger.info("Order payment is cancelling for order id: ${order.id.id}")
        return OrderCancelledEvent(
            order = order,
            createdAt = ZonedDateTime.now(ZoneId.of(ZONE_KO))
        )
    }

    override fun cancelOrder(order: Order, failureMessage: List<String>) {
        order.cancel(failureMessage)
        logger.info("Order with id: ${order.id.id} is cancelled")
    }
}
