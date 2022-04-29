package com.rp199.reactivekafka.config

import com.rp199.reactivekafka.handler.OtherMessageHandler
import com.rp199.reactivekafka.handler.SampleMessageHandler
import org.springframework.web.reactive.function.server.coRouter

fun router(sampleMessageHandler: SampleMessageHandler, otherMessageHandler: OtherMessageHandler) = coRouter {
    POST("/publish/{key}") {
        sampleMessageHandler.handlePublishMessage(it)
    }

    POST("/publish/other/{key}") {
        otherMessageHandler.handlePublishMessage(it)
    }
}