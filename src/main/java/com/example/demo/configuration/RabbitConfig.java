package com.example.demo.configuration;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
	
	@Bean
    Queue bookingQueue() {
        return new Queue("bookingQueue",false);
    }
	
	@Bean
	TopicExchange exchange() {
		return new TopicExchange("bookingExchange");
	}
	
	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("routingkey");
	}
	
	@Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    } 
	
	@Bean
	AmqpTemplate template(ConnectionFactory connectionFactory) {
		
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
		
		
		
	}

}
