package food.ordering.system.order.service.domain.dto.message

import com.food.ordering.system.domain.valueobject.OrderStatusApproval
import java.time.Instant

data class RestaurantApprovalResponse(
    val id: String,
    val sagaId: String,
    val orderId: String,
    val restaurant: String,
    val createdAt: Instant,
    val orderApprovalStatus: OrderStatusApproval,
    val failureMessages: List<String>,
)
