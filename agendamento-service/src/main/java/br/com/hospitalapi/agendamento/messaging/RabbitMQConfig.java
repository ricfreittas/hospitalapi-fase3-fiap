package br.com.hospitalapi.agendamento.messaging;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "appointment.exchange";
    public static final String QUEUE = "appointment.notification.queue";
    public static final String ROUTING_KEY = "appointment.notification";

    @Bean
    public DirectExchange appointmentExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue appointmentQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding appointmentBinding(
            Queue appointmentQueue,
            DirectExchange appointmentExchange
    ) {
        return BindingBuilder
                .bind(appointmentQueue)
                .to(appointmentExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}