package food.ordering.system.order.service.domain.ports.output.message.publisher.payment

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent

interface OrderCreatedPaymentRequestMessagePublisher : DomainEventPublisher<OrderCreatedEvent> {
}
