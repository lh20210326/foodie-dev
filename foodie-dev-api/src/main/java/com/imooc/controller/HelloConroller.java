package com.imooc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloConroller {
    final static Logger logger=LoggerFactory.getLogger(HelloConroller.class);
    @GetMapping("/hello")
    public Object hello() {
        logger.debug("debug:hello");
        logger.info("info:hello");
        logger.warn("warn:hello");
        logger.error("error:hello");
        return "hello liuhao";
    }
}

