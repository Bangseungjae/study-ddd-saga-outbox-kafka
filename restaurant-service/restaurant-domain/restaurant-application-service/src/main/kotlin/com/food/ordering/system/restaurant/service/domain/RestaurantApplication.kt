package com.food.ordering.system.restaurant.service.domain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["com.food.ordering.system.restaurant.service.domain.config"])
class RestaurantApplication
