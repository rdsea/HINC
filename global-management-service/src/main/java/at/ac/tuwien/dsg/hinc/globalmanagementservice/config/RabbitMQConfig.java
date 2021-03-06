package at.ac.tuwien.dsg.hinc.globalmanagementservice.config;

import at.ac.tuwien.dsg.hinc.globalmanagementservice.messagehandlers.HandleControlResult;
import at.ac.tuwien.dsg.hinc.globalmanagementservice.messagehandlers.HandleDeliverProviders;
import at.ac.tuwien.dsg.hinc.globalmanagementservice.messagehandlers.HandleDeliverResources;
import at.ac.tuwien.dsg.hinc.globalmanagementservice.messagehandlers.HandleRegisterLMS;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${hinc.global.rabbitmq.input}")
    private String inputExchange;

    @Value("${hinc.global.rabbitmq.input}")
    private String inputQueue;

    @Value("${hinc.global.rabbitmq.output.broadcast}")
    private String outputBroadcast;

    @Value("${hinc.global.rabbitmq.output.groupcast}")
    private String outputGroupcast;

    @Value("${hinc.global.rabbitmq.output.unicast}")
    private String outputUnicast;


    @Bean
    Queue inputQueue() {
        //TODO queue settings
        return new Queue(inputQueue,false);
    }

    @Bean
    FanoutExchange inputExchange() {
        //TODO exchange settings
        return new FanoutExchange(inputExchange);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(inputQueue()).to(inputExchange());
    }

    @Bean
    FanoutExchange broadcastExchange(){
        //TODO exchange settings
        return new FanoutExchange(outputBroadcast);
    }

    @Bean
    DirectExchange groupcastExchange(){
        //TODO exchange settings
        return new DirectExchange(outputGroupcast);
    }

    @Bean
    DirectExchange unicastExchange(){
        //TODO exchange settings
        return new DirectExchange(outputUnicast);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(inputQueue);
        container.setMessageListener(listenerAdapter);
        container.setDefaultRequeueRejected(false);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(HandleRegisterLMS handleRegisterLMS,
                                           HandleDeliverProviders handleDeliverProviders,
                                           HandleDeliverResources handleDeliverResources,
                                           HandleControlResult handleControlResult) {
        handleRegisterLMS.addMessageHandler(handleDeliverResources);
        handleRegisterLMS.addMessageHandler(handleDeliverProviders);
        handleRegisterLMS.addMessageHandler(handleControlResult);

        return new MessageListenerAdapter(handleRegisterLMS, "handleMessage");
    }

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReplyTimeout(600000);
        template.setUseTemporaryReplyQueues(true);
        return template;
    }
}
