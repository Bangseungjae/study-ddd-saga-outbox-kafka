package com.food.ordering.system.customer.service

import com.food.ordering.system.customer.service.domain.CustomerDomainService
import com.food.ordering.system.customer.service.domain.CustomerDomainServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun customerDomainService(): CustomerDomainService = CustomerDomainServiceImpl()
}
