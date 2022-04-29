package com.rp199.reactivekafka.kafka

import reactor.kafka.sender.SenderResult

interface CoReactiveKafkaProducer<K, V> {
    suspend fun send(key: K, value: V): SenderResult<Void>
}