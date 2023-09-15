package it.unisalento.smartcitywastemanagement.disposalms.configuration;


import it.unisalento.smartcitywastemanagement.disposalms.exceptions.MessageArgumentNotValidException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryException;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitMQConfiguration {

    // CONFIGURAZIONE CODA DI AGGIUNTA CONFERIMENTO
    @Value("${rabbitmq.queue.addDisposal}")
    private String addDisposalQueue;


    // CONFIGURAZIONE CODA DI CONFERMA CONFERIMENTO
    @Value("${rabbitmq.queue.confirmDisposal}")
    private String confirmDisposalQueue;

    @Value("${rabbitmq.routingKey.confirmRouting}")
    private String confirmRoutingKey;


    // CONFIGURAZIONE CODA DI COMUNICAZIONE CON SMARTBIN_MS
    @Value("${rabbitmq.queue.smartBinUpdate}")
    private String smartBinUpdateQueue;

    @Value("${rabbitmq.routingKey.smartBinUpdate}")
    private String smartBinUpdateRoutingKey;


    @Value("${rabbitmq.exchange.directType}")
    private String directExchange;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;



    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host); // Imposta l'URL del server RabbitMQ
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username); // Imposta il nome utente
        connectionFactory.setPassword(password); // Imposta la password
        return connectionFactory;
    }



    @Bean
    public Queue addDisposalQueue() {
        return new Queue(addDisposalQueue, true,false,false);
    }

    @Bean
    public Queue confirmDisposalQueue() {
        return new Queue(confirmDisposalQueue, true);
    }

    @Bean
    public Queue smartBinUpdateQueue() {
        return new Queue(smartBinUpdateQueue, true);
    }


    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directExchange);
    }



    @Bean
    public Binding bindingConfirmQueue() {
        return BindingBuilder.bind(confirmDisposalQueue()).to(directExchange()).with(confirmRoutingKey);
    }

    @Bean
    public Binding bindingSmartBinUpdateQueue() {
        return BindingBuilder.bind(smartBinUpdateQueue()).to(directExchange()).with(smartBinUpdateRoutingKey);
    }


    // Template con politica di configurazione di retry per l'invio fissata a 3 tentativi
    @Bean
    public RabbitTemplate customRabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());

        // Configura una politica di ritentativo personalizzata
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // Imposta il numero massimo di tentativi

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);

        template.setRetryTemplate(retryTemplate);

        return template;
    }






}
