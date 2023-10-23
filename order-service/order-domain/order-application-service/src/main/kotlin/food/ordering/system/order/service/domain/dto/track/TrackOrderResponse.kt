package food.ordering.system.order.service.domain.dto.track

import com.food.ordering.system.domain.valueobject.OrderStatus
import java.util.UUID

data class TrackOrderResponse(
    val orderTrackingId: UUID,
    val orderStatus: OrderStatus,
    val failureMessages: List<String>,
)
