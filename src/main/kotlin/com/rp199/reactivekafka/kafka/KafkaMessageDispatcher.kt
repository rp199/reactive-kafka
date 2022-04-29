package com.rp199.reactivekafka.kafka

import com.rp199.reactivekafka.handler.OtherMessageHandler
import com.rp199.reactivekafka.handler.SampleMessageHandler
import com.rp199.reactivekafka.handler.model.OtherMessage
import com.rp199.reactivekafka.handler.model.SampleMessage
import org.slf4j.LoggerFactory

interface KafkaMessageDispatcher<K, V> {
    suspend fun dispatch(key: K, value: V)
}

class KafkaStringAnyMessageDispatcher(
    private val sampleMessageHandler: SampleMessageHandler,
    private val otherMessageHandler: OtherMessageHandler
) :
    KafkaMessageDispatcher<String, Any> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override suspend fun dispatch(key: String, value: Any) {
        when (value) {
            is SampleMessage -> sampleMessageHandler.handleReceiveMessage(key, value)
            is OtherMessage -> otherMessageHandler.handleReceiveMessage(key, value)
            else -> logger.warn("Received message with unknown type: $value")
        }
    }

}