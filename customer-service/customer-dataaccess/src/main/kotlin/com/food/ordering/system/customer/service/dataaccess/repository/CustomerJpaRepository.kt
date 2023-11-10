package com.food.ordering.system.customer.service.dataaccess.repository

import com.food.ordering.system.customer.service.dataaccess.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CustomerJpaRepository : JpaRepository<CustomerEntity, UUID> {
}
