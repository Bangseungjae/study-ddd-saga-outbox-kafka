package com.food.ordering.system.customer.service.application.rest

import com.food.ordering.system.customer.service.domain.create.CreateCustomerCommand
import com.food.ordering.system.customer.service.domain.create.CreateCustomerResponse
import com.food.ordering.system.customer.service.domain.ports.input.service.CustomerApplicationService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/customers"], produces = ["application/vnd.api.v1+json"])
class CustomerController(
    private val customerApplicationService: CustomerApplicationService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    fun createCustomer(
        @RequestBody createCustomerCommand: CreateCustomerCommand,
    ): ResponseEntity<CreateCustomerResponse> {
        logger.info("Creating customer with username: ${createCustomerCommand.username}")
        val response = customerApplicationService.createCustomer(createCustomerCommand)
        return ResponseEntity.ok().body(response)
    }
}
