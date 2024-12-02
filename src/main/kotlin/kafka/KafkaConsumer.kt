package com.salus.kafka

import kafka.consumer.KafkaConsumers


object KafkaConsumer {

    private val consumer = KafkaConsumers<String, Any>("localhost:9092", "group-auth-api", "topic-auth-api")

    fun consumer() {
        try {
            consumer.consumer { record ->
                println("Message received: ${record.value()}")
            }
        } catch (e: Exception) {
            println("Error consuming message: ${e.message}")
        } finally {
            consumer.close()
        }
    }
}

