package com.food.ordering.system.payment.service.dataaccess.creditentry.adapter

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.payment.service.dataaccess.creditentry.mapper.CreditEntryDataAccessMapper
import com.food.ordering.system.payment.service.dataaccess.creditentry.repository.CreditEntryJpaRepository
import com.food.ordering.system.payment.service.domain.entity.CreditEntry
import com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy.CreditEntryRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class CreditEntryRepositoryImpl(
    private val creditEntryJpaRepository: CreditEntryJpaRepository,
    private val creditEntryDataAccessMapper: CreditEntryDataAccessMapper,
    private val entityManager: EntityManager,
) : CreditEntryRepository {
    override fun save(creditEntry: CreditEntry): CreditEntry =
        creditEntryDataAccessMapper.creditEntryEntityToCreditEntry(
            creditEntryJpaRepository.save(creditEntryDataAccessMapper.creditEntryToCreditEntryEntity(creditEntry))
        )

    override fun findByCustomerId(customerId: CustomerId): CreditEntry? =
        creditEntryJpaRepository.findByCustomerId(customerId.value)
            ?.let { creditEntryDataAccessMapper.creditEntryEntityToCreditEntry(it) }

    override fun detach(customerId: CustomerId) {
        entityManager.detach(
            creditEntryJpaRepository.findByCustomerId(customerId.value)
                ?: throw IllegalArgumentException()
        )
    }
}
