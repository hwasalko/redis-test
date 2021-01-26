package com.hwasalko.redis.controller;

import com.hwasalko.redis.config.RedisUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RedisController {
    
    public static Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    RedisUtil redisUtil;

    @GetMapping("/")
    public String hello() throws InterruptedException {

        logger.info(" Hello Redis {}!", "aaa");


        try {
            redisUtil.setDataExpire("ch", "11111", 5);
        
            redisUtil.getData("ch"); 
            Thread.sleep(1000);

            redisUtil.getData("ch"); 
            Thread.sleep(1000);

            redisUtil.getData("ch"); 
            Thread.sleep(1000);

            redisUtil.getData("ch"); 
            Thread.sleep(1000);

            redisUtil.getData("ch"); 
            Thread.sleep(1000);

            redisUtil.getData("ch"); 
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        logger.info(" Hello Redis {}!", "bbb");

        return "hello redis";
    }


    

}
