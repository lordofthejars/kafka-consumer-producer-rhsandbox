package org.acme;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import static java.util.Collections.unmodifiableList;

@Path("/produces")
public class MyReactiveMessagingApplication {

    public static final List<String> NAMES;

    static  {
        try(final InputStream nameInputStream = MyReactiveMessagingApplication.class.getClassLoader().getResourceAsStream("names.txt")) {
            if (nameInputStream == null) {
                throw new IOException("names list not found");
            }
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(nameInputStream))) {
                final List<String> names = new ArrayList<>();
                while(reader.ready()) {
                    names.add(reader.readLine());
                }
                NAMES = unmodifiableList(names);
            }

        } catch (IOException e) {
            throw new IllegalStateException("Error while loading name list", e);
        }
    }

    @Channel("names")
    Emitter<Record<String, String>> emitter;

    @GET
    public void generate() {
        NAMES.stream()
        .map(n -> Record.of(Integer.toString(n.hashCode()), n))
        .forEach(record -> emitter.send(record));
    }

}
