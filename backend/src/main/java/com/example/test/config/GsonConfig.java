package com.example.test.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;

class InstantTypeAdapter extends TypeAdapter<Instant> {

    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        return Instant.parse(in.nextString());
    }
}

@Configuration
public class GsonConfig {
    @Bean
    protected Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();
    }
}