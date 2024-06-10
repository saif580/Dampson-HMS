package com.hms.appointments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "appointments_db"; // Replace with your database name
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new StringToLocalTimeConverter());
        converters.add(new LocalTimeToStringConverter());
        return new MongoCustomConversions(converters);
    }

    @Bean
    public Converter<String, LocalTime> stringToLocalTimeConverter() {
        return new StringToLocalTimeConverter();
    }

    @Bean
    public Converter<LocalTime, String> localTimeToStringConverter() {
        return new LocalTimeToStringConverter();
    }

    static class StringToLocalTimeConverter implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String source) {
            return LocalTime.parse(source);
        }
    }

    static class LocalTimeToStringConverter implements Converter<LocalTime, String> {
        @Override
        public String convert(LocalTime source) {
            return source.toString();
        }
    }
}
