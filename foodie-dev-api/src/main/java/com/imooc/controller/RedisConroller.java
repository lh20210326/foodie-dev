package com.imooc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("redis")
public class RedisConroller {
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/delete")
    public Object delete(String key) {
        redisTemplate.delete(key);
        return "OK";
    }

    @GetMapping("/set")
    public Object set(String key,String value) {
        redisTemplate.opsForValue().set(key,value);
        return "OK";
    }

    @GetMapping("/get")
    public String get(String key) {

        return (String) redisTemplate.opsForValue().get(key);
    }
}

