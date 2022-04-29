package com.rp199.reactivekafka.kafka

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import reactor.kafka.receiver.ReceiverRecord
import java.util.concurrent.Executors

abstract class CoReactiveKafkaListener<K, V>(
    private val kafkaMessageDispatcher: KafkaMessageDispatcher<K, V>,
    coroutineDispatcher: CoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val coroutineScope = CoroutineScope(coroutineDispatcher)

    abstract suspend fun receive(): Flow<ReceiverRecord<K, V>>

    fun listen() {
        logger.info("Starting kafka listener")
        coroutineScope.launch {
            receive().onEach {
                logger.info("Message received. Key: ${it.key()}, Value: ${it.value()}")
                kafkaMessageDispatcher.dispatch(it.key(), it.value())
            }.onEach {
                it.receiverOffset().acknowledge()
            }.retryWhen { cause, attempt ->
                logger.warn("Could not process message, attempt: $attempt. Retrying", cause)
                delay(1000)
                true
            }.count()
        }
    }
}