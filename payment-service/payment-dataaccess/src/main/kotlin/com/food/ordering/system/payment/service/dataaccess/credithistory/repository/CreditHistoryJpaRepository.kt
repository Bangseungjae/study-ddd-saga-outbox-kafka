package com.food.ordering.system.payment.service.dataaccess.credithistory.repository

import com.food.ordering.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CreditHistoryJpaRepository : JpaRepository<CreditHistoryEntity, UUID>{
    fun findByCustomerId(customerId: UUID): MutableList<CreditHistoryEntity>
}
