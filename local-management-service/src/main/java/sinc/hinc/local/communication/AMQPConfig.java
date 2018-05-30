package sinc.hinc.local.communication;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sinc.hinc.local.communication.messagehandlers.*;

@Configuration
public class AMQPConfig {

    @Value("${adaptor.amqp.input}")
    private String adaptorInputExchange;

    @Value("${adaptor.amqp.input}")
    private String adaptorInputQueue;

    @Value("${adaptor.amqp.output.broadcast}")
    private String adaptorOutputBroadcastExchange;

    @Value("${adaptor.amqp.output.unicast}")
    private String adaptorOutputUnicastExchange;

    @Value("${hinc.local.group}")
    private String group;

    @Value("${hinc.local.id}")
    private String id;

    @Value("${hinc.local.amqp.output}")
    private String hincOutputExchange;

    @Bean
    Queue adaptorInputQueue() {
        //TODO queue settings
        return new Queue(adaptorInputQueue,false);
    }

    @Bean
    Queue hincInputQueue() {
        //TODO queue settings
        return new Queue(group+"."+id,false);
    }

    @Bean
    FanoutExchange adaptorInputExchange() {
        //TODO exchange settings
        return new FanoutExchange(adaptorInputExchange);
    }

    @Bean
    FanoutExchange broadcastExchange(){
        //TODO exchange settings
        return new FanoutExchange(adaptorOutputBroadcastExchange);
    }

    @Bean
    DirectExchange unicastExchange(){
        //TODO exchange settings
        return new DirectExchange(adaptorOutputUnicastExchange);
    }

    @Bean
    FanoutExchange hincOutputExchange() {
        //TODO exchange settings
        return new FanoutExchange(hincOutputExchange);
    }

    @Bean
    Binding adaptorBinding() {
        return BindingBuilder.bind(adaptorInputQueue()).to(adaptorInputExchange());
    }


    @Bean
    SimpleMessageListenerContainer hincContainer(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter hincListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(group+"."+id);
        container.setMessageListener(hincListenerAdapter);
        container.setDefaultRequeueRejected(false);
        return container;
    }

    @Bean
    MessageListenerAdapter hincListenerAdapter(HandleFetchResources handleFetchResources,
                                               HandleFetchProviders handleFetchProviders,
                                               HandleProvision handleProvision,
                                               HandleDelete handleDelete) {

        handleFetchProviders.addMessageHandler(handleFetchResources);
        handleFetchProviders.addMessageHandler(handleProvision);
        handleFetchProviders.addMessageHandler(handleDelete);

        return new MessageListenerAdapter(handleFetchProviders, "handleMessage");
    }

    @Bean
    SimpleMessageListenerContainer adaptorContainer(ConnectionFactory connectionFactory,
                                                 MessageListenerAdapter adaptorListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(adaptorInputQueue);
        container.setMessageListener(adaptorListenerAdapter);
        container.setDefaultRequeueRejected(false);
        return container;
    }

    @Bean
    MessageListenerAdapter adaptorListenerAdapter(HandleResourcesUpdate handleResourcesUpdate,
                                                  HandleProviderUpdate handleProviderUpdate,
                                                  HandleControlResult handleControlResult,
                                                  HandleRegisterAdaptor handleRegisterAdaptor,
                                                  HandleDeregisterAdaptor handleDeregisterAdaptor) {

        handleResourcesUpdate.addMessageHandler(handleProviderUpdate);
        handleResourcesUpdate.addMessageHandler(handleControlResult);
        handleResourcesUpdate.addMessageHandler(handleRegisterAdaptor);
        handleResourcesUpdate.addMessageHandler(handleDeregisterAdaptor);
        return new MessageListenerAdapter(handleResourcesUpdate, "handleMessage");
    }

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReplyTimeout(600000);
        return template;
    }

}
