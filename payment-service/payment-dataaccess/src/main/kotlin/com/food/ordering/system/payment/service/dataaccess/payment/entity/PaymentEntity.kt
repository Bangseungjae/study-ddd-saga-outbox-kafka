package com.food.ordering.system.payment.service.dataaccess.payment.entity

import com.food.ordering.system.domain.valueobject.PaymentStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID

@Table(name = "payments")
@Entity
class PaymentEntity(
    @Id
    val id: UUID,

    val customerId: UUID,
    val orderId: UUID,
    val price: BigDecimal,

    @Enumerated(EnumType.STRING)
    val status: PaymentStatus,
    val createdAt: ZonedDateTime,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PaymentEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
