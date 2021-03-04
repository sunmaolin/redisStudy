package sml.string;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * String计算测试
 */
public class StringCalRedis {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        ValueOperations valueOperations = redisTemplate.opsForValue();

        valueOperations.set("key","1");

        // incr key 原来的值+1
        // incrby key increment  原来的值+increment
        // incrbyfloat key increment 原来的值+increment（float类型）
        // Spring对此进行了加强，可以加任何长整型与float类型
        valueOperations.increment("key",2);
        //valueOperations.increment("key",1.1);
        System.out.println(valueOperations.get("key"));

        // decr key 原来的值-1
        // decrby key decrement 原来的值-decrement
        // Spring并没有支持减法，需要使用以下方法
        redisTemplate.getConnectionFactory().getConnection()
                .decr(redisTemplate.getKeySerializer().serialize("key"));
        redisTemplate.getConnectionFactory().getConnection()
                .decrBy(redisTemplate.getKeySerializer().serialize("key"),2);
        System.out.println(valueOperations.get("key"));
    }
}
