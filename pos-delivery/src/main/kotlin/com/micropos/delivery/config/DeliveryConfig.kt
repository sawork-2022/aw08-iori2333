package com.micropos.delivery.config

import com.micropos.delivery.service.KoujiDeliveryService
import com.micropos.model.Order
import com.micropos.model.OrderStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class DeliveryConfig {
    @Autowired
    private lateinit var deliveryService: KoujiDeliveryService

    @Autowired
    private lateinit var streamBridge: StreamBridge

    private val logger = LoggerFactory.getLogger(DeliveryConfig::class.java)

    @Bean
    fun onOrdering(): Consumer<Order> = Consumer {
        logger.info("Delivering $it")
        when (it.status) {
            OrderStatus.ToSend -> deliveryService.createDelivery(it)
            OrderStatus.ToCancel -> deliveryService.cancelDelivery(it)
            else -> return@Consumer
        }
        streamBridge.send("orderCreated", it)
    }
}
