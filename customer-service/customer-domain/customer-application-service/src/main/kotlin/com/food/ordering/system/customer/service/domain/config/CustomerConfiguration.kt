package com.food.ordering.system.customer.service.domain.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration


@ConfigurationPropertiesScan(basePackages = [
    "com.food.ordering.system.customer.service.domain.config"
])
@Configuration
class CustomerConfiguration
