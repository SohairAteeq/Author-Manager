package com.sohair.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entityType) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if (o == null) {
                log.info("Key '{}' not found in Redis", key);
                return null; // or return a default value
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(o.toString(), entityType);
        } catch (Exception e) {
            log.error("Error retrieving value from Redis for key: {}", key, e);
            return null;
        }
    }

    public void set(String key, Object value, long ttl){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        }
        catch(Exception e){
            log.error("Error setting value in Redis for key: {}", key, e);
        }
    }
}
