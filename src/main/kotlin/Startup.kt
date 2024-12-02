package com.salus

import com.salus.kafka.KafkaConsumer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class Startup

fun main(args: Array<String>) {
    runApplication<Startup>(*args)
    KafkaConsumer.consumer()
}