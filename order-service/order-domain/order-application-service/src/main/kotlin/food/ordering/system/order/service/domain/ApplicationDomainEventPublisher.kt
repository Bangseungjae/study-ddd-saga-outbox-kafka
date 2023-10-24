package food.ordering.system.order.service.domain

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationEventPublisherAware
import org.springframework.stereotype.Component

@Component
class ApplicationDomainEventPublisher(
) : ApplicationEventPublisherAware,
    DomainEventPublisher<OrderCreatedEvent> {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    override fun publish(domainEvent: OrderCreatedEvent) {
        applicationEventPublisher.publishEvent(domainEvent)
        logger.info("OrderCreatedEvent is published for order id: ${domainEvent.order.id.value}")
    }

    override fun setApplicationEventPublisher(applicationEventPublisher: ApplicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher
    }
}
