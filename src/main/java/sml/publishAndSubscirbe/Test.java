package sml.publishAndSubscirbe;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

public class Test {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        String channel = "chat";
        redisTemplate.convertAndSend(channel,"测试发布订阅");
    }
}
