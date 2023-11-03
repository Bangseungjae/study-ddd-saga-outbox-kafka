package com.food.ordering.system.payment.service.domain

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun paymentDomainService(): PaymentDomainService {
        return PaymentDomainServiceImpl()
    }
}
