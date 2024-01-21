package com.example.registration.Configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration
{
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    @Value("${spring.kafka.topic2.name}")
    private String topicNameUserDeletion;
    // spring bean for kafka topic
    @Bean
    public NewTopic topic()
    {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic topicNameUserDeletion()
    {
        return TopicBuilder.name(topicNameUserDeletion)
                .partitions(1)
                .replicas(3)
                .build();
    }
}
