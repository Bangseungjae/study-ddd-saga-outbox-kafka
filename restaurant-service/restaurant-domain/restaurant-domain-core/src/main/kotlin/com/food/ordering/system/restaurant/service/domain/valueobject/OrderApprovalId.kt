package com.food.ordering.system.restaurant.service.domain.valueobject

import com.food.ordering.system.domain.valueobject.BaseId
import java.util.UUID

class OrderApprovalId(
    value: UUID
) : BaseId<UUID>(value)
