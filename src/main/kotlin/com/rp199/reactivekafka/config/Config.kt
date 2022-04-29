package com.rp199.reactivekafka.config

import com.rp199.reactivekafka.handler.OtherMessageHandler
import com.rp199.reactivekafka.handler.SampleMessageHandler
import com.rp199.reactivekafka.handler.topicName
import com.rp199.reactivekafka.kafka.CoReactiveKafkaListener
import com.rp199.reactivekafka.kafka.KafkaStringAnyMessageDispatcher
import com.rp199.reactivekafka.kafka.reactor.CoReactorKafkaListener
import com.rp199.reactivekafka.kafka.reactor.CoReactorKafkaProducer
import com.rp199.reactivekafka.kafka.template.CoReactiveKafkaTemplateListener
import com.rp199.reactivekafka.kafka.template.CoReactiveKafkaTemplateProducer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import java.util.concurrent.Executors

fun initBeans() = beans {
    bean<SampleMessageHandler>()
    bean<OtherMessageHandler>()
    bean { router(ref(), ref()) }

    bean<KafkaStringAnyMessageDispatcher>()

    bean<CoroutineDispatcher>("kafkaListenerCoroutineDispatchers") {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    kafkaOptions()
    reactiveKafkaTemplate()

    profile("reactor-kafka") {
        reactorKafka()
    }

    bean {
        CommandLineRunner {
            ref<CoReactiveKafkaListener<String, Any>>().listen()
        }
    }
}

fun BeanDefinitionDsl.kafkaOptions() {
    bean {
        val kafkaProperties = ref<KafkaProperties>()
        SenderOptions.create<String, Any>(kafkaProperties.buildProducerProperties())
    }

    bean {
        val kafkaProperties = ref<KafkaProperties>()
        ReceiverOptions.create<String, Any>(kafkaProperties.buildConsumerProperties())
            .subscription(listOf(topicName))
    }
}

fun BeanDefinitionDsl.reactorKafka() {

    bean<KafkaSender<String, Any>> {
        KafkaSender.create(ref())
    }

    bean<KafkaReceiver<String, Any>> {
        KafkaReceiver.create(ref<ReceiverOptions<String, Any>>())
    }

    bean(isPrimary = true) {
        CoReactorKafkaProducer<String, Any>(topicName, ref())
    }

    bean<CoReactorKafkaListener<String, Any>>(isPrimary = true)
}

fun BeanDefinitionDsl.reactiveKafkaTemplate() {
    bean<ReactiveKafkaProducerTemplate<String, Any>>()

    bean<ReactiveKafkaConsumerTemplate<String, Any>>()

    bean {
        CoReactiveKafkaTemplateProducer<String, Any>(topicName, ref())
    }

    bean<CoReactiveKafkaTemplateListener<String, Any>>()
}