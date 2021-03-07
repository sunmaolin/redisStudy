package sml.expire;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * redis 超时设置
 */
public class ExpireRedis {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        redisTemplate.opsForValue().set("key","value");
        System.out.println(redisTemplate.opsForValue().get("key"));

        // ttl key 获取超时时间  -1没有超时时间  -2已超时
        System.out.println(redisTemplate.getExpire("key"));

        // expire key seconds 设置超时时间
        redisTemplate.expire("key",2, TimeUnit.SECONDS);
        System.out.println("超时时间："+redisTemplate.getExpire("key"));
//        Thread.sleep(2000);
//        //此时返回-2已超时
//        System.out.println(redisTemplate.getExpire("key"));

        // persist key 取消超时时间
        redisTemplate.persist("key");
        // 超时时间已取消，返回 -1
        System.out.println(redisTemplate.getExpire("key"));

        //设置超时时间点
        long now = System.currentTimeMillis();
        Date date = new Date();
        //当前时间加上120s
        date.setTime(now+120000);
        // expireat key timestamp
        redisTemplate.expireAt("key",date);

        Thread.sleep(1000);
        // 阻塞一秒，当前时间应该是119秒超时
        System.out.println(redisTemplate.getExpire("key"));



    }
}
