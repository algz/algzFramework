package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.service.HelloService;

@Controller
public class HelloController {
	
	@Autowired
	private HelloService helloService;
	
    @RequestMapping(value="/hello")
    public ModelAndView  printHello() {
        return new ModelAndView("hello", "message", "Hello Spring to "+helloService.printHello()+" !");
    }
}
