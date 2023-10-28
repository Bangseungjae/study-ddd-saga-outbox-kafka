package com.food.ordering.system.order.service.application.rest

import food.ordering.system.order.service.domain.dto.create.CreateOrderCommand
import food.ordering.system.order.service.domain.dto.create.CreateOrderResponse
import food.ordering.system.order.service.domain.dto.track.TrackOrderQuery
import food.ordering.system.order.service.domain.dto.track.TrackOrderResponse
import food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(value = ["/orders"], produces = ["application/vnd.api.v1+json"])
class OrderController(
    private val orderApplicationService: OrderApplicationService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    fun createOrderResponse(@RequestBody createOrderCommand: CreateOrderCommand): ResponseEntity<CreateOrderResponse> {
        logger.info("Creating order for customer: ${createOrderCommand.customerId} at restaurant: ${createOrderCommand.restaurantId}")

        val createOrderResponse = orderApplicationService.createOrder(createOrderCommand)
        return ResponseEntity.ok(createOrderResponse)
    }
    @GetMapping("/{trackingId}")
    fun getOrderByTrackingId(@PathVariable trackingId: UUID): ResponseEntity<TrackOrderResponse> {
        val trackOrderQuery = TrackOrderQuery(trackingId)
        val trackOrderResponse = orderApplicationService.trackOrder(trackOrderQuery)
        logger.info("Returning order status with tracking id: ${trackOrderResponse.orderTrackingId}")
        return ResponseEntity.ok(trackOrderResponse)
    }
}
