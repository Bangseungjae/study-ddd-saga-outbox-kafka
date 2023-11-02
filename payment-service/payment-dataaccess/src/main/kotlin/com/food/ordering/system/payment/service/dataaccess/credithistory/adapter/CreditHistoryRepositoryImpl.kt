package com.food.ordering.system.payment.service.dataaccess.credithistory.adapter

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.payment.service.dataaccess.credithistory.mapper.toCreditHistory
import com.food.ordering.system.payment.service.dataaccess.credithistory.mapper.toCreditHistoryEntity
import com.food.ordering.system.payment.service.dataaccess.credithistory.repository.CreditHistoryJpaRepository
import com.food.ordering.system.payment.service.domain.entity.CreditHistory
import com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy.CreditHistoryRepository
import org.springframework.stereotype.Component

@Component
class CreditHistoryRepositoryImpl(
    private val creditHistoryJpaRepository: CreditHistoryJpaRepository,
) : CreditHistoryRepository {
    override fun save(creditHistory: CreditHistory): CreditHistory =
        creditHistoryJpaRepository.save(creditHistory.toCreditHistoryEntity())
            .toCreditHistory()

    override fun findByCustomerId(customerId: CustomerId): MutableList<CreditHistory> {
        val creditHistoryEntities = creditHistoryJpaRepository.findByCustomerId(customerId.value)
        return creditHistoryEntities.map { it.toCreditHistory() }.toMutableList()
    }
}
