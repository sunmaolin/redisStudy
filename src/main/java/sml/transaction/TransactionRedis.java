package sml.transaction;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.List;

/**
 * redis事务管理
 */
public class TransactionRedis {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

//        testTransaction(redisTemplate);
        testRedisPipeline(redisTemplate);
    }


    /**
     * 测试事务命令
     * @param redisTemplate
     */
    public static void testTransaction(RedisTemplate redisTemplate){
        SessionCallback callback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                //multi 开启事务
                redisOperations.multi();
                //操作命令入队
                redisOperations.opsForValue().set("key","value");
                //由于命令只是进入队列，而没有被执行，所以此处采用get命令返回值却是null
                String value = (String) redisOperations.opsForValue().get("key");
                System.out.println(value);
                //exec 该list会保存之前进入队列的所有命令的结果
                List list = redisOperations.exec();
                // 事务结束后，返回所需要的结果，这里是返回所有的结果
                return list;
            }
        };

        // 执行redis命令
        List list = (List) redisTemplate.execute(callback);
        System.out.println(list.toString());
    }

    /**
     * 测试redis流水线，解决等待网络消耗的问题
     * @param redisTemplate
     */
    public static void testRedisPipeline(RedisTemplate redisTemplate){
        long start = System.currentTimeMillis();
        SessionCallback callback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.multi();
                for (int i = 0; i < 100000; i++) {
                    redisOperations.opsForValue().set("key"+i,"value"+i);
                    redisOperations.opsForValue().get("key"+i);
                }
                redisOperations.exec();
                return null;
            }
        };
        //执行redeis的流水线命令
        redisTemplate.executePipelined(callback);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
