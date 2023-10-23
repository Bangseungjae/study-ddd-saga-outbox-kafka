package food.ordering.system.order.service.domain

import food.ordering.system.order.service.domain.dto.track.TrackOrderQuery
import food.ordering.system.order.service.domain.dto.track.TrackOrderResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderTrackCommandHandler {
    val logger = LoggerFactory.getLogger(this.javaClass)

    fun trackOrder(trackOrderQuery: TrackOrderQuery): TrackOrderResponse {
        TODO()
    }
}
