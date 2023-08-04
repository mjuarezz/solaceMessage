package com.mjz.solace.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjz.solace.model.Customer;
import com.mjz.solace.service.SolaceMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

public class JmsMessageListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(JmsMessageListener.class);

    @Autowired
    private SolaceMessageService messageService;

    @Override
    public void onMessage(Message message) {
        String messageData;
        Customer customer;
        if(message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)message;
            try {
                messageData = textMessage.getText();
                logger.info(messageData);
                //message.acknowledge();
                messageService.processSolaceMessage(textMessage.getText());
                
                /*
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    customer = objectMapper.readValue(messageData, Customer.class);
                    if(customer == null) {
                        logger.error("Invalid message from the solace queue");
                    }else {
                        logger.info("Successfully parsed solace message to object.");
                        messageService.processSolaceMessage(customer);
                    }
                } catch (IOException e) {
                    logger.error("Error while parsing JSON from Solace.");
                    e.printStackTrace();
                }
                */
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }else {
            logger.info(message.toString());
            logger.info("Invalid message. Skipping ....");
        }

    }
	
	
}
