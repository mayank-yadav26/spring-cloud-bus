package com.springbus.servicea;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for Spring Cloud Bus.
 * Spring Cloud Bus automatically creates the exchange and queues.
 * We configure the message converter for proper JSON serialization/deserialization
 * of RemoteApplicationEvent instances.
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
