package com.mapr.examples;

import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This producer will send a bunch of messages to topic "fast-messages". Every so often,
 * it will send a message to "slow-messages". This shows how messages can be sent to
 * multiple topics. On the receiving end, we will see both kinds of messages but will
 * also see how the two topics aren't really synchronized.
 */
public class Producer {
    public static void main(String[] args) throws IOException {

        final String TOPIC_FAST_MESSAGES = "/user/user01/sample-stream:fast-messages";
        final String TOPIC_SUMMARY_MARKERS = "/user/user01/sample-stream:summary-markers";

        // set up the producer
        KafkaProducer<String, String> producer;
        try (InputStream props = Resources.getResource("producer.props").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<>(properties);
        }
      
      try {
            for (int i = 0; i < 1000; i++) {
                // send lots of messages
                
                producer.send(new ProducerRecord<String, String>(
                        TOPIC_FAST_MESSAGES,
                        String.format("{\"type\":\"test\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
                System.out.println("Sent msg  " + i + " to Topic " + TOPIC_FAST_MESSAGES);
                // every so often send to a different topic
                if (i % 100 == 0) {
                    producer.send(new ProducerRecord<String, String>(
                            TOPIC_FAST_MESSAGES,
                            String.format("{\"type\":\"marker\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
                    System.out.println("Sent msg  " + i + " to Topic " + TOPIC_FAST_MESSAGES);
                    producer.send(new ProducerRecord<String, String>(
                            TOPIC_SUMMARY_MARKERS,
                            String.format("{\"type\":\"other\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
                    System.out.println("Sent msg  " + i + " to Topic " + TOPIC_SUMMARY_MARKERS);
                    producer.flush();      
                }
            }
        } catch (Throwable throwable) {
            System.out.printf("%s", throwable.getStackTrace());
        } finally {
            producer.close();
        }

    }
}
