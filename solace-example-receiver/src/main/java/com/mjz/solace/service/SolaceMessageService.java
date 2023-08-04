package com.mjz.solace.service;

import com.mjz.solace.model.Customer;

public interface SolaceMessageService {
	   //void processSolaceMessage(Customer customer);
	void processSolaceMessage(String msg);
	
}
