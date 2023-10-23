package food.ordering.system.order.service.domain.ports.input.service

import food.ordering.system.order.service.domain.dto.create.CreateOrderCommand
import food.ordering.system.order.service.domain.dto.create.CreateOrderResponse
import food.ordering.system.order.service.domain.dto.track.TrackOrderQuery
import food.ordering.system.order.service.domain.dto.track.TrackOrderResponse

interface OrderApplicationService {

    fun createOrder(createOrderCommand: CreateOrderCommand): CreateOrderResponse

    fun trackOrder(trackOrderQuery: TrackOrderQuery): TrackOrderResponse
}
