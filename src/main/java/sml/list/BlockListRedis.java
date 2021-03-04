package sml.list;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 链表阻塞命令
 */
public class BlockListRedis {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        ListOperations listOperations = redisTemplate.opsForList();

        List<String> nodeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            nodeList.add("node"+i);
        }
        listOperations.leftPushAll("list",nodeList);

        // blpop key timeout 左边弹出，若列表没有元素，会阻塞列表直到存在元素或时间超时
        // 第二个参数为超时时间，第三个参数为超时时间单位
        listOperations.leftPop("list",1, TimeUnit.SECONDS);
        // brpop key timeout
        listOperations.rightPop("list",1,TimeUnit.SECONDS);

        //在创建一个链表
        listOperations.leftPushAll("list1",nodeList);
        // rpoplpush list1 list2 将list1最右边元素移除，添加到list2最左边
        listOperations.rightPopAndLeftPush("list","list1");
        // brpoplpush list1 list2 阻塞，可设置超时时间
        listOperations.rightPopAndLeftPush("list","list1",1,TimeUnit.SECONDS);


        redisTemplate.delete("list");
    }
}
