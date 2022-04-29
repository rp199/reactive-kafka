package com.rp199.reactivekafka.handler

import com.rp199.reactivekafka.handler.model.SampleMessage
import com.rp199.reactivekafka.kafka.CoReactiveKafkaProducer
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import reactor.kafka.sender.SenderResult

const val topicName = "test-topic"

class SampleMessageHandler(
    private val coReactiveKafkaProducer: CoReactiveKafkaProducer<String, Any>
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    
    suspend fun handlePublishMessage(request: ServerRequest): ServerResponse {
        val key = request.pathVariable("key")
        val message = request.awaitBody<SampleMessage>()

        val result: SenderResult<*> = coReactiveKafkaProducer.send(key, message)
        logger.info("Sender result: $result")
        return ServerResponse.ok().bodyValueAndAwait("ok")
    }

    fun handleReceiveMessage(key: String, sampleMessage: SampleMessage) {
        logger.info("Received sample message with $key and body $sampleMessage")
    }
}