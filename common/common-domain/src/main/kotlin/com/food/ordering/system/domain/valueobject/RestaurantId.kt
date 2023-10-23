package com.food.ordering.system.domain.valueobject

import java.util.UUID

data class RestaurantId(
    val id: UUID
) : BaseId<UUID>(id)
