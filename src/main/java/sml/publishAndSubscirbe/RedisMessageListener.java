package sml.publishAndSubscirbe;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 发布订阅监听类
 */
public class RedisMessageListener implements MessageListener {

    private RedisTemplate redisTemplate;

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        // 获取消息
        byte[] body = message.getBody();
        // 使用值序列化器转化
        String msgBody = (String) getRedisTemplate().getValueSerializer().deserialize(body);
        System.out.println(msgBody);

        // 获取channel  渠道
        byte[] channel = message.getChannel();
        //使用字符串序列化器转化
        String channelStr = (String) getRedisTemplate().getStringSerializer().deserialize(channel);
        System.out.println(channelStr);

        //渠道名称转化
        String bytesStr = new String(bytes);
        System.out.println(bytesStr);
    }
}
