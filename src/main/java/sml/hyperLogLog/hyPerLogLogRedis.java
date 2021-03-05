package sml.hyperLogLog;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;

/**
 * 基数 不重复
 */
public class hyPerLogLogRedis {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        HyperLogLogOperations hyperLogLogOperations = redisTemplate.opsForHyperLogLog();

        // hfadd key element 添加，若不存在添加，不存在不添加
        System.out.println(hyperLogLogOperations.add("key", "a", "b"));

        System.out.println(hyperLogLogOperations.add("key", "a","b"));

        // hfcount key 元素个数
        System.out.println(hyperLogLogOperations.size("key"));

        // pfmerge deskey key1[key2...] 合并基数key1 key2 到deskey
        hyperLogLogOperations.add("key1","c","d");
        System.out.println(hyperLogLogOperations.union("deskey", "key", "key1"));

        redisTemplate.delete(Arrays.asList("deskey","key","key1"));
    }
}
