package com.mjz.solace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.solacesystems.jms.SolConnectionFactory;

@SpringBootApplication
public class SolaceExampleSenderApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(SolaceExampleSenderApplication.class, args);
	}
	
}
