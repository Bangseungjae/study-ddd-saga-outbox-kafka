package com.food.ordering.system.payment.service.dataaccess.creditentry.mapper

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.domain.valueobject.Money
import com.food.ordering.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity
import com.food.ordering.system.payment.service.domain.entity.CreditEntry
import com.food.ordering.system.payment.service.domain.valueobject.CreditEntryId
import org.springframework.stereotype.Component

@Component
class CreditEntryDataAccessMapper {
    fun creditEntryEntityToCreditEntry(creditEntryEntity: CreditEntryEntity): CreditEntry = CreditEntry(
        creditEntryId = CreditEntryId(creditEntryEntity.id),
        customerId = CustomerId(creditEntryEntity.customerId),
        totalCreditAmount = Money(creditEntryEntity.totalCreditAmount),
    )

    fun creditEntryToCreditEntryEntity(creditEntry:  CreditEntry): CreditEntryEntity = CreditEntryEntity(
        id = creditEntry.id.value,
        customerId = creditEntry.customerId.value,
        totalCreditAmount = creditEntry.totalCreditAmount.amount,
    )
}
