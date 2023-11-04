package com.food.ordering.system.restaurant.service.domain

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun restaurantDomainService(): RestaurantDomainService = RestaurantDomainServiceImpl()
}
