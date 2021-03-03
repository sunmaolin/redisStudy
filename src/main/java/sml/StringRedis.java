package sml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class StringRedis {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        ValueOperations valueOperations = redisTemplate.opsForValue();

        valueOperations.set("key","value");
        System.out.println(valueOperations.get("key"));
        

    }
}
