package sml.set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 有序集合  set集合是无序，这是有序
 */
public class ZsetRedis {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        //Spring提供接口TypedTuple操作有序集合
        Set<ZSetOperations.TypedTuple> set1 = new HashSet<ZSetOperations.TypedTuple>();
        Set<ZSetOperations.TypedTuple> set2 = new HashSet<ZSetOperations.TypedTuple>();
        //造点数据
        int j = 9;
        for (int i = 1; i <= 9; i++) {
            j--;
            //算出分数和值
            Double score1 = Double.valueOf(i);
            String value1 = "x" + i;
            Double score2 = Double.valueOf(j);
            String value2 = (j % 2 == 1 ? "y" : "x") + i;
            //使用Spring提供的默认TypedTuple-DefaultTypedTuple
            ZSetOperations.TypedTuple typedTuple1 = new DefaultTypedTuple(value1,score1);
            set1.add(typedTuple1);
            ZSetOperations.TypedTuple typedTuple2 = new DefaultTypedTuple(value2,score2);
            set2.add(typedTuple2);
        }

        // zadd key score1 value1 [score2,value2...]将元素插入
        zSetOperations.add("zset1",set1);
        zSetOperations.add("zset2",set2);

        // zcard key 统计总数
        System.out.println(zSetOperations.size("zset1"));

        // zcount key min max 求 min<=score<=max 的值的数量
        System.out.println(zSetOperations.count("zset1",1,3));

        Set set = null;

        // zrange key start stop 按分数从小到大排序后，根据 start<=下标<=stop 返回
        set = zSetOperations.range("zset1",1,2);
        printSet(set);
        // zrange key start stop withscores
        set = zSetOperations.rangeWithScores("zset1",0,-1);
        printTypedTuple(set);


        //区间设置
        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
        // 小于
        range.lt("x8");
        // 大于
        range.gt("x1");
        // zrangebylex key min max 根据字典排序，取值
        set = zSetOperations.rangeByLex("zset1",range);
        printSet(set);
        // 小于等于
        range.lte("x8");
        // 大于等于
        range.gte("x1");
        set = zSetOperations.rangeByLex("zset1",range);
        printSet(set);
        // 限制返回个数
        RedisZSetCommands.Limit limit = new RedisZSetCommands.Limit();
        // 限制返回个数
        limit.count(5);
        // 偏移量，从第二个开始截取
        limit.offset(2);
        // zrangebylex key min max limit offset count
        set = zSetOperations.rangeByLex("zset1",range,limit);
        printSet(set);

        // zrank key member 求排行，排名第一返回0 第二返回1
        System.out.println(zSetOperations.rank("zset1", "x1"));

        // zrem key member[member2...] 删除元素，返回删除个数
        System.out.println(zSetOperations.remove("zset2", "x2","x4"));

        // zremrangebyrank key start stop 按照排行删除
        zSetOperations.removeRange("zset2",0,1);
        printTypedTuple(zSetOperations.rangeByScore("zset2",0,-1));

        // zincrby key increment member 给集合中的一个分数+11
        zSetOperations.incrementScore("zset1","x1",11);

        // zremrangebyscore key start stop 按照分数范围删除
        zSetOperations.removeRangeByScore("zset1",1,3);

        // zrevrangebyscore key start stop
        set = zSetOperations.reverseRangeByScoreWithScores("zset1",1,10);
        printTypedTuple(set);


        // zinterstore desKey numberKeys key1 [key2..] 将numberKeys个集合的交集放到desKey中
        zSetOperations.intersectAndStore("zset1","zset2","inter_zset");



        redisTemplate.delete(Arrays.asList("zset1","zset2","inter_zset"));

    }

    /**
     * 打印TypedTuple集合
     * @param set
     */
    public static void printTypedTuple(Set<ZSetOperations.TypedTuple> set){
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            ZSetOperations.TypedTuple typedTuple = (ZSetOperations.TypedTuple) iterator.next();
            System.out.println("score:"+typedTuple.getScore()+",value"+typedTuple.getValue());
        }
    }

    /**
     * 打印set集合
     * @param set
     */
    public static void printSet(Set set){
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            String value = (String) iterator.next();
            System.out.print(value+"\t");
        }
        System.out.println();
    }
}
