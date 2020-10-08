package com.gstone.sms;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {
    public void sendSms(Map<String, String> message){
        System.out.println("SMS Monitor is listening");
        System.out.println("Phone number: "+message.get("phone"));
        System.out.println("code: "+message.get("code"));
    }
}
