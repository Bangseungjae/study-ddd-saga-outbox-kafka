package food.ordering.system.order.service.domain.dto.create

import java.math.BigDecimal
import java.util.UUID

data class CreateOrderCommand(
    val customerId: UUID,
    val restaurantId: UUID,
    val price: BigDecimal,
    val items: List<OrderItem>,
    val address: OrderAddress
)
