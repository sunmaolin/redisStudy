package sml.list;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 链表  相当于LinkedList  有序的
 */
public class ListRedis {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        ListOperations listOperations = redisTemplate.opsForList();

        // lpush key value1[value2...]
        listOperations.leftPush("list","node0");

        List<String> nodeList = new ArrayList<>();
        for (int i = 1; i < 3; i++ ){
            nodeList.add("node"+i);
        }
        listOperations.leftPushAll("list",nodeList);

        // rpush key value1[value2...]
        listOperations.rightPush("list","node3");

        // lindex key index 获取下标为0的节点
        System.out.println(listOperations.index("list", 0));

        // llen key 获取链表长度
        System.out.println(listOperations.size("list"));

        // lpop key 从左边弹出一个节点
        System.out.println(listOperations.leftPop("list"));
        // rop key
        System.out.println(listOperations.rightPop("list"));

        //注意：需要使用底层命令才能使用插入
        // linsert key before/after node(某个值之前) value（要插入的值）
        redisTemplate.getConnectionFactory().getConnection()
                .lInsert("list".getBytes(StandardCharsets.UTF_8), RedisListCommands.Position.BEFORE,"node1".getBytes(StandardCharsets.UTF_8),"insertNode".getBytes(StandardCharsets.UTF_8));
        System.out.println(listOperations.index("list",0));

        // lpushx key node 存在key的list则插入
        listOperations.leftPushIfPresent("list","start");
        // rpushx key node
        listOperations.rightPush("list","end");

        // lrange key start end 返回下标范围元素
        List<String> valueList = listOperations.range("list",0,10);
        System.out.println(valueList.toString());

        // lrem key count value 删除count个值为xx的节点
        listOperations.remove("list",1,"node1");

        // lset key index node 下标为index设置新值
        listOperations.set("list",0,"newStart");
        System.out.println(listOperations.index("list", 0));


        redisTemplate.delete("list");

    }
}
