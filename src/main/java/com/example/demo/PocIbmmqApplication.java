package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableEurekaClient
@SpringBootApplication
@RestController
@EnableJms
public class PocIbmmqApplication {

	@Autowired
	private JmsTemplate jmsTemplate;

	public static void main(String[] args) {
		SpringApplication.run(PocIbmmqApplication.class, args);
	}

	@GetMapping("send")
	String send() {
		try {
			jmsTemplate.convertAndSend("DEV.QUEUE.1", System.currentTimeMillis());
			return "OK";
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@GetMapping("recv")
	String recv() {
		try {
			return jmsTemplate.receiveAndConvert("DEV.QUEUE.1").toString();
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@JmsListener(destination = "DEV.QUEUE.1")
	public void onReceiverQueue(String str) {
		System.out.println(str);
	}

}
