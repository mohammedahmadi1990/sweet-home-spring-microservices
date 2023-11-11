package com.sweethome.notificationService;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

public class Consumer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.server", "ec2-user@ec2-3-228-137-169.compute-1.amazonaws.com:9092");
        properties.setProperty("group.id", "sweethome");
        properties.setProperty("enable.auto.commit", "true");
        properties.setProperty("auto.commit.intervals.ms", "1000");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String > consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList("message"));

        Set<String> subscribedTopics = consumer.subscription();
        for(String topic : subscribedTopics){
            System.out.println(topic);
        }

        try{
            while(true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records){
                    System.out.println(record.value());
                }
            }
        }finally {
            consumer.close();
        }
    }
}
