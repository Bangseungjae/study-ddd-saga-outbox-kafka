package com.food.ordering.system.order.service.domain

import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher
import com.food.ordering.system.order.service.domain.ports.output.repository.*
import org.mockito.Mockito
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackages = ["com.food.ordering.system"])
class OrderTestConfiguration {

    @Bean
    fun paymentRequestMessagePublisher(): PaymentRequestMessagePublisher {
        return Mockito.mock(PaymentRequestMessagePublisher::class.java)
    }

    @Bean
    fun restaurantApprovalRequestMessagePublisher(): RestaurantApprovalRequestMessagePublisher {
        return Mockito.mock(RestaurantApprovalRequestMessagePublisher::class.java)
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
    fun paymentOutboxRepository(): PaymentOutboxRepository {
        return Mockito.mock(PaymentOutboxRepository::class.java)
    }

    @Bean
    fun approvalOutboxRepository(): ApprovalOutboxRepository {
        return Mockito.mock(ApprovalOutboxRepository::class.java)
    }

    @Bean
    fun orderDomainService(): OrderDomainService {
        return OrderDomainServiceImpl()
    }

}
