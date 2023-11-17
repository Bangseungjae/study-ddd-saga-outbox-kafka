package com.food.ordering.system.payment.service.dataaccess.creditentry.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.math.BigDecimal
import java.util.UUID

@Table(name = "credit_entry")
@Entity
class CreditEntryEntity(
    @Id
    val id: UUID,
    val customerId: UUID,
    val totalCreditAmount: BigDecimal,
    @Version
    val version: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CreditEntryEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
