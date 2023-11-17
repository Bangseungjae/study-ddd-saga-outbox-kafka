package com.food.ordering.system.order.service.dataaccess.order.repository

import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OrderJpaRepository : JpaRepository<OrderEntity, UUID> {

    fun findByTrackingId(trackingId: UUID): OrderEntity?
}
