package com.food.ordering.system.domain.valueobject

import java.util.UUID

data class ProductId(
    val id: UUID
) : BaseId<UUID>(id)
