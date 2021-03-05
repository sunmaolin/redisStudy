package sml.set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.Arrays;
import java.util.Set;

/**
 * 集合，相当于hashSet  无序，不可重复，每个值都是String类型
 */
public class SetRedis {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        SetOperations setOperations = redisTemplate.opsForSet();
        Set<String> set = null;

        // sadd key member1 [member2...] 将元素加入列表
        setOperations.add("set1","mem1","mem2","mem3","mem4");
        setOperations.add("set2","mem2","mem3","mem4","mem5");

        // scard key 求集合长度
        System.out.println(setOperations.size("set1"));

        // sdiff key1 key2 求差集
        set = setOperations.difference("set1","set2");
        System.out.println(set.toString());

        // sdiffstore des key1 key2 求差集并保存到des中,跟命令反着来
        setOperations.differenceAndStore("set1","set2","set3");
        System.out.println(setOperations.size("set3"));

        // sinter key1 key2 求交集
        set = setOperations.intersect("set1","set2");
        System.out.println(set.toString());

        // sinterstore des key1 key2
        setOperations.intersectAndStore("set1","set2","set3");
        System.out.println(setOperations.size("set3"));

        // sunion key1 key2 求并集
        set = setOperations.union("set1","set2");
        System.out.println(set.toString());

        // sunion des key1 key2
        setOperations.unionAndStore("set1","set2","set3");
        System.out.println(setOperations.size("set3"));

        // sismember key member 是否集合中的元素
        System.out.println(setOperations.isMember("set1", "mem2"));

        // smembers key 返回集合中的所有元素
        set = setOperations.members("set1");
        System.out.println(set.toString());

        // spop key 集合中随机弹出一个元素
        System.out.println(setOperations.pop("set3"));

        // srandmember key [count] 随机从集合中获取count个元素
        System.out.println(setOperations.randomMember("set3"));
        System.out.println(setOperations.randomMembers("set3",2).toString());

        // srem key member1 [member2...] 删除元素
        setOperations.remove("set3","mem2","mem1");


        redisTemplate.delete(Arrays.asList("set1","set2","set3"));
    }
}
