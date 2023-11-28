package org.acme;


import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.*;

import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class MyReactiveMessagingApplication {

    
    @Incoming("names")
    public CompletionStage<Void> consume(Message<String> message) {
        Optional<IncomingKafkaRecordMetadata> x = message.getMetadata(IncomingKafkaRecordMetadata.class);
        x.ifPresent(m -> System.out.println("Partition: " + m.getPartition()));
        System.out.println("Message: " + message.getPayload());
        return message.ack();
    }

}
