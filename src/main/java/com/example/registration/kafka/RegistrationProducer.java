package com.example.registration.kafka;

import com.example.registration.Events.UserCreatedEvent;
import com.example.registration.Events.UserDeletedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationProducer.class);
    private NewTopic topic;

    private NewTopic topicNameUserDeletion;

    private KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    private KafkaTemplate<String, UserDeletedEvent> kafkaTemplateUserDeletion;

    private ObjectMapper ObjectMapperJson;

    public RegistrationProducer(NewTopic topic, KafkaTemplate<String, UserCreatedEvent> kafkaTemplate, ObjectMapper objectMapper, KafkaTemplate<String, UserDeletedEvent> kafkaTemplate2, NewTopic topicNameUserDeletion)
    {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.ObjectMapperJson = objectMapper;
        this.kafkaTemplateUserDeletion = kafkaTemplate2;
        this.topicNameUserDeletion = topicNameUserDeletion;
    }

    public void SendMessage(Object accountEvent)
    {
        try {
            String jsonPayload = ObjectMapperJson.writeValueAsString(accountEvent);

            LOGGER.info(String.format("Registration event => %s", jsonPayload));

            Message<String> message = MessageBuilder
                    .withPayload(jsonPayload)
                    .setHeader(KafkaHeaders.TOPIC, topic.name())
                    .build();
            kafkaTemplate.send(message);

        } catch (JsonProcessingException e) {

            LOGGER.error("Error serializing payload to JSON", e);
        }
    }

    public void SendMessageUserDeletion(Object accountDeleteEvent)
    {
        try {
            String jsonPayload = ObjectMapperJson.writeValueAsString(accountDeleteEvent);

            LOGGER.info(String.format("Account delete event => %s", jsonPayload));

            Message<String> message = MessageBuilder
                    .withPayload(jsonPayload)
                    .setHeader(KafkaHeaders.TOPIC, topicNameUserDeletion.name())
                    .build();
            kafkaTemplateUserDeletion.send(message);

        } catch (JsonProcessingException e) {

            LOGGER.error("Error serializing payload to JSON", e);
        }
    }
}