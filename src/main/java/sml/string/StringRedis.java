package sml.string;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class StringRedis {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        ValueOperations valueOperations = redisTemplate.opsForValue();

        // set key value
        valueOperations.set("key","value");
        // get key value
        System.out.println(valueOperations.get("key"));

        // strlen key 求长度
        System.out.println(valueOperations.size("key"));

        // getset key 设置新值，返回旧值(key不存在，直接设置)
        String oldValue = (String) valueOperations.getAndSet("key","newValue");
        System.out.println(oldValue);

        // getrange key start end 求子串
        System.out.println(valueOperations.get("key",0,1));

        // append key 追加字符串到末尾，返回新长度
        Integer newLen = valueOperations.append("key","appendValue");
        System.out.println(newLen);

        // delete key
        redisTemplate.delete("key");

    }
}
