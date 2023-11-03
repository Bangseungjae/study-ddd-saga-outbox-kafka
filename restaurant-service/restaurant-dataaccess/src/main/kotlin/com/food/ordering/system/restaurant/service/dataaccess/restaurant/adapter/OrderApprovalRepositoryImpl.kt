package com.food.ordering.system.restaurant.service.dataaccess.restaurant.adapter

import com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper.toOrderApproval
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper.toOrderApprovalEntity
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.repository.OrderApprovalJpaRepository
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository
import org.springframework.stereotype.Component

@Component
class OrderApprovalRepositoryImpl(
    private val orderApprovalJpaRepository: OrderApprovalJpaRepository,
) : OrderApprovalRepository {
    override fun save(orderApproval: OrderApproval): OrderApproval {
        return orderApprovalJpaRepository
            .save(orderApproval.toOrderApprovalEntity())
            .toOrderApproval()
    }
}
