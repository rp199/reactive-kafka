package com.rp199.reactivekafka.kafka.reactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.apache.kafka.clients.producer.ProducerRecord
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult

suspend fun <K, V> KafkaSender<K, V>.sendSingle(
    topicName: String,
    key: K,
    message: V,
): SenderResult<Void> =
    mono(Dispatchers.Unconfined) {
        val producerRecord = ProducerRecord(topicName, key, message)
        SenderRecord.create<K, V, Void>(producerRecord, null)
    }.let {
        send(it)
    }.awaitSingle()
