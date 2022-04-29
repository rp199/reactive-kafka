package com.rp199.reactivekafka.kafka.reactor

import com.rp199.reactivekafka.kafka.CoReactiveKafkaProducer
import org.slf4j.LoggerFactory
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderResult

class CoReactorKafkaProducer<K : Any, V : Any>(
    private val topicName: String,
    private val kafkaSender: KafkaSender<K, V>
) :
    CoReactiveKafkaProducer<K, V> {
    private val logger = LoggerFactory.getLogger(javaClass)
    
    override suspend fun send(key: K, value: V): SenderResult<Void> {
        logger.info("Sending message with key $key and value $value")
        return kafkaSender.sendSingle(topicName, key, value)
    }
}