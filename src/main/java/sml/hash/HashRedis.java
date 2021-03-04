package sml.hash;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * 类似于HashMap  无序
 */
public class HashRedis {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        HashOperations hashOperations = redisTemplate.opsForHash();

        Map<String,String> map = new HashMap<>();
        map.put("key1","value1");
        map.put("key2","value2");

        // hmset key field1 value1 [field2,value2...]
        hashOperations.putAll("hash",map);

        // hset key field value
        hashOperations.put("hash","key3","3");

        // hsetnx key field value 不存在才插入
        boolean isSuccess = hashOperations.putIfAbsent("hash","key3","4");
        System.out.println(isSuccess);

        // hexists key field 是否存在
        System.out.println(hashOperations.hasKey("hash", "key1"));

        // hgetall key 获取所有键值对
        map = hashOperations.entries("hash");
        System.out.println(map.get("key1"));

        // hget key field
        System.out.println(hashOperations.get("hash", "key1"));

        // hmget key field1[field2...]
        List<String> someValue = hashOperations.multiGet("hash", Arrays.asList("key1","key2"));
        System.out.println(someValue.get(0));

        // hincr key field 值-1
        // hincrby key field increment 值-increment
        // hincrbyfloat key field increment 值-increment(float类型)
        hashOperations.increment("hash","key3",2);
        hashOperations.increment("hash","key3",0.5);
        System.out.println(hashOperations.get("hash","key3"));

        // hkeys key 获取所有的键
        Set keySets = hashOperations.keys("hash");
        System.out.println(keySets.toArray()[0]);

        // hvals key 获取所有的值
        List<String> valueList = hashOperations.values("hash");
        System.out.println(valueList.get(0));

        // hdel key field1[field2...] 删除键值对
        hashOperations.delete("hash","key1");
        System.out.println(hashOperations.get("hash","key1"));

        redisTemplate.delete("hash");

    }
}
