package com.yuri.hexagonal.adapters.out;

import com.yuri.hexagonal.application.ports.out.SendCpfForValidationOutputPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendCpfValidationAdapter implements SendCpfForValidationOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SendCpfValidationAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String cpf) {
        kafkaTemplate.send("tp-cpf-validation", cpf);
    }
}
