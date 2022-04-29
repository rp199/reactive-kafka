package com.rp199.reactivekafka.kafka.template

import com.rp199.reactivekafka.kafka.CoReactiveKafkaProducer
import kotlinx.coroutines.reactive.awaitFirst
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderResult

class CoReactiveKafkaTemplateProducer<K : Any, V : Any>(
    private val topicName: String,
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<K, V>
) :
    CoReactiveKafkaProducer<K, V> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override suspend fun send(key: K, value: V): SenderResult<Void> {
        logger.info("Sending message with key $key and value $value")
        return reactiveKafkaProducerTemplate.send(topicName, key, value).awaitFirst()
    }
}