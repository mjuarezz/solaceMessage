package com.mjz.solace.config;

import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;


@Configuration
@PropertySource({"classpath:application.properties"})
public class BeanConfig {

    private static final Logger logger = LoggerFactory.getLogger(BeanConfig.class);

    @Autowired
    private Environment environment;



    @Bean
    public SolConnectionFactory solConnectionFactory() throws Exception {
        SolConnectionFactory connectionFactory = SolJmsUtility.createConnectionFactory();
        connectionFactory.setHost(environment.getProperty("solace.java.host"));
        connectionFactory.setVPN(environment.getProperty("solace.java.msgVpn"));
        connectionFactory.setUsername(environment.getProperty("solace.java.clientUsername"));
        connectionFactory.setPassword(environment.getProperty("solace.java.clientPassword"));
        connectionFactory.setClientID(environment.getProperty("solace.java.clientName"));
        return connectionFactory;
    }



    @Bean(destroyMethod = "close")
    public Connection connection() {
        Connection connection = null;
        javax.jms.Session session;
        try {
            connection = solConnectionFactory().createConnection();
            session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            
            System.out.printf("Connected to the Solace Message VPN '%s' with client username '%s'.%n", 
            		environment.getProperty("solace.java.msgVpn"),
            		environment.getProperty("solace.java.clientUsername"));
            
            Queue queue = session.createQueue(environment.getProperty("solace.message.consumer.queue"));
            
            // Create the message producer for the created queue
            MessageProducer messageProducer = session.createProducer(queue);
            
            for (int i = 0; i < 10; i++) {
            	sendSimpleMessage(messageProducer, queue, session, "Hello world Queues! => " + i);	
			}
            
            // Close everything in the order reversed from the opening order
            // NOTE: as the interfaces below extend AutoCloseable,
            // with them it's possible to use the "try-with-resources" Java statement
            // see details at https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
            messageProducer.close();
            session.close();
            connection.close();    	

        } catch (Exception e) {
            logger.info("JMS connection failed with Solace." + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    private void sendSimpleMessage(MessageProducer messageProducer, Queue queue, javax.jms.Session session, String msg) throws JMSException {
        // Create a text message.
        TextMessage message = session.createTextMessage(msg);
        
        System.out.printf("Sending message '%s' to queue '%s'...%n", message.getText(), queue.toString());

        // Send the message
        // NOTE: JMS Message Priority is not supported by the Solace Message Bus
        messageProducer.send(queue, message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY,
                Message.DEFAULT_TIME_TO_LIVE);

        System.out.println("Sent successfully. Exiting...");
    	
    }
    
    /*
    private void sendMessage(MessageProducer messageProducer, Queue queue, javax.jms.Session session) throws JMSException {
    	
    	JmsTemplate jmsTemplate;
    	
        Customer cust = new Customer("1", "Juan", "Perez");
        
        
        Map<String, Object> map = new HashMap<>(); 
        map.put("id", cust.getId()); map.put("firstName", cust.getFirstName()); map.put("lastName", cust.getLastName()); 
        jmsTemplate.convertAndSend(map);         


    	// Create a text message.
        TextMessage message = session.createTextMessage("Hola");
        
        System.out.printf("Sending message '%s' to queue '%s'...%n", message.getText(), queue.toString());

        // Send the message
        // NOTE: JMS Message Priority is not supported by the Solace Message Bus
        messageProducer.send(queue, message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY,
                Message.DEFAULT_TIME_TO_LIVE);

        System.out.println("Sent successfully. Exiting...");
    	
    }
    */
    


}
