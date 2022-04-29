package com.rp199.reactivekafka.kafka.reactor

import com.rp199.reactivekafka.kafka.CoReactiveKafkaListener
import com.rp199.reactivekafka.kafka.KafkaMessageDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverRecord
import java.util.concurrent.Executors

class CoReactorKafkaListener<K, V>(
    private val kafkaReceiver: KafkaReceiver<K, V>,
    kafkaMessageDispatcher: KafkaMessageDispatcher<K, V>,
    coroutineDispatcher: CoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
) : CoReactiveKafkaListener<K, V>(kafkaMessageDispatcher, coroutineDispatcher) {

    override suspend fun receive(): Flow<ReceiverRecord<K, V>> {
        return kafkaReceiver.receive().asFlow()
    }
}