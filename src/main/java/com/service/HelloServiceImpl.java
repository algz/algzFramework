package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HelloServiceImpl implements HelloService {

	@Autowired
	private HelloDao helloDao;
	
	@Transactional
	public String printHello() {
		return "this service "+helloDao.inputDate();
	}

}
