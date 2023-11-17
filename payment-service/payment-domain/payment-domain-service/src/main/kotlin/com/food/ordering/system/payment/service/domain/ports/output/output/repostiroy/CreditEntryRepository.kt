package com.food.ordering.system.payment.service.domain.ports.output.output.repostiroy

import com.food.ordering.system.domain.valueobject.CustomerId
import com.food.ordering.system.payment.service.domain.entity.CreditEntry

interface CreditEntryRepository {

    fun save(creditEntry: CreditEntry): CreditEntry

    fun findByCustomerId(customerId: CustomerId): CreditEntry?

    fun detach(customerId: CustomerId)
}
