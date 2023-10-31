package com.food.ordering.system.payment.service.domain.valueobject

import com.food.ordering.system.domain.valueobject.BaseId
import java.util.UUID

data class PaymentId(
    val id: UUID,
): BaseId<UUID>(id)
