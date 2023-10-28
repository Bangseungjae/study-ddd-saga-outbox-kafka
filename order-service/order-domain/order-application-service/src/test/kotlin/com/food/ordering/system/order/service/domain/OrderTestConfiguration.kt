package com.food.ordering.system.order.service.domain

import food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher
import food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher
import food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher
import food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository
import food.ordering.system.order.service.domain.ports.output.repository.OrderRepository
import food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository
import org.mockito.Mockito
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackages = ["food.ordering.system"])
class OrderTestConfiguration {

    @Bean
    fun orderCreatedPaymentRequestMessagePublisher(): OrderCreatedPaymentRequestMessagePublisher {
        return Mockito.mock(OrderCreatedPaymentRequestMessagePublisher::class.java)
    }

    @Bean
    fun orderCancelledPaymentRequestMessagePublisher(): OrderCancelledPaymentRequestMessagePublisher {
        return Mockito.mock(OrderCancelledPaymentRequestMessagePublisher::class.java)
    }

    @Bean
    fun orderPaidRequestMessagePublisher(): OrderPaidRestaurantRequestMessagePublisher {
        return Mockito.mock(OrderPaidRestaurantRequestMessagePublisher::class.java)
    }

    @Bean
    fun orderRepository(): OrderRepository {
        return Mockito.mock(OrderRepository::class.java)
    }

    @Bean
    fun customerRepository(): CustomerRepository {
        return Mockito.mock(CustomerRepository::class.java)
    }

    @Bean
    fun restaurantRepository(): RestaurantRepository {
        return Mockito.mock(RestaurantRepository::class.java)
    }

    @Bean
    fun orderDomainService(): OrderDomainService {
        return OrderDomainServiceImpl()
    }

}
