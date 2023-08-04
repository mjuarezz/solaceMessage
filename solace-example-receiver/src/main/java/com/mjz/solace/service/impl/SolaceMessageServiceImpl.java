package com.mjz.solace.service.impl;

//import com.mjz.solace.model.Customer;
import com.mjz.solace.service.SolaceMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SolaceMessageServiceImpl implements SolaceMessageService {

	private static final Logger logger = LoggerFactory.getLogger(SolaceMessageServiceImpl.class);
	
	@Override
	//public void processSolaceMessage(Customer customer) {
	public void processSolaceMessage(String msg) {
		//logger.info(customer.getId());
		logger.info("Message : " + msg);
		
	}

}
