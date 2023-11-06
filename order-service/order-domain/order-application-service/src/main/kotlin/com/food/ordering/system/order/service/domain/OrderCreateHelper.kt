package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand
import com.food.ordering.system.order.service.domain.entity.Customer
import com.food.ordering.system.order.service.domain.entity.Order
import com.food.ordering.system.order.service.domain.entity.Restaurant
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent
import com.food.ordering.system.order.service.domain.exception.OrderDomainException
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class OrderCreateHelper(
    private val orderDomainService: OrderDomainService,
    private val orderRepository: OrderRepository,
    private val customerRepository: CustomerRepository,
    private val restaurantRepository: RestaurantRepository,
    private val orderDataMapper: OrderDataMapper,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


    @Transactional
    fun persistOrder(createdOrderCommand: CreateOrderCommand): OrderCreatedEvent {
        checkCustomer(createdOrderCommand.customerId)
        val restaurant = checkRestaurant(createdOrderCommand)
        val order = orderDataMapper.createOrderCommandToOrder(createdOrderCommand)
        val orderCreatedEvent = orderDomainService.validateAndInitiateOrder(
            order = order,
            restaurant = restaurant
        )
        saveOrder(order)
        logger.info("Order is created with id: ${orderCreatedEvent.order.id.value}")
        return orderCreatedEvent
    }

    private fun checkRestaurant(createOrderCommand: CreateOrderCommand): Restaurant {
        val restaurant: Restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)
        val findRestaurantInformation: Restaurant? = restaurantRepository.findRestaurantInformation(restaurant)
        if (findRestaurantInformation == null) {
            throwDomainExceptionAndLogging("Cloud not find restaurant with restaurant id: ${createOrderCommand.restaurantId}")
        }
        return findRestaurantInformation!!
    }

    private fun throwDomainExceptionAndLogging(message: String) {
        logger.info(message)
        throw OrderDomainException(message)
    }

    private fun checkCustomer(customerId: UUID) {
        val customer: Customer? = customerRepository.findCustomer(customerId)
        customer ?: run {
            logger.info("Could not find customer with customer id: $customerId")
            throw OrderDomainException("Could not find customer with customer id: $customerId")
        }
    }

    private fun saveOrder(order: Order): Order {
        val orderResult: Order = orderRepository.save(order)
        logger.info("Order is saved with id: ${orderResult.id.value}")
        return orderResult
    }
}
