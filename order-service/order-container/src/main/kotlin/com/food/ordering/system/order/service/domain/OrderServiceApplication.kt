package com.food.ordering.system.order.service.domain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@EnableJpaRepositories(basePackages = [
    "com.food.ordering.system.order.service.dataaccess",
    "com.food.ordering.system.dataaccess",
])
@EntityScan(basePackages = [
    "com.food.ordering.system.order.service.dataaccess",
    "com.food.ordering.system.dataaccess",
])
@SpringBootApplication(scanBasePackages = ["com.food.ordering.system"])
class OrderServiceApplication {
}

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}
