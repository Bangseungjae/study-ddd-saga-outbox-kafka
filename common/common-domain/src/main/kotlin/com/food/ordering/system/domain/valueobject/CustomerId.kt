package com.food.ordering.system.domain.valueobject

import java.util.UUID

data class CustomerId(
    val id: UUID
) : BaseId<UUID>(id)
