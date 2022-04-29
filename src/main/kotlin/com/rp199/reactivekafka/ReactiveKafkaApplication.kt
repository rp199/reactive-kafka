package com.rp199.reactivekafka

import com.rp199.reactivekafka.config.initBeans
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveKafkaApplication

fun main(args: Array<String>) {
    runApplication<ReactiveKafkaApplication>(*args) {
        addInitializers(initBeans())
    }
}
