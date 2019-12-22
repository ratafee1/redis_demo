package cn.itcast.jedis;


import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisOperate {
    private JedisPool jedisPool;

    @BeforeTest
    public void connectJedis() {
        //创建数据库连接池对象
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(10);//设置连接redis的最大空闲数
        jedisPoolConfig.setMaxWaitMillis(5000);//设置连接redis-超时时间
        jedisPoolConfig.setMaxTotal(50);//设置redis连接最大客户端数
        jedisPoolConfig.setMinIdle(5);
        //获取jedis连接池
        jedisPool = new JedisPool(jedisPoolConfig, "node01", 6379);
    }

    @Test
    public void testSet () {
        final Jedis jedis = jedisPool.getResource();
        jedis.sadd("setkey", "1", "2", "3");
        final Set<String> result = jedis.smembers("setkey");
        for (String s : result) {
            System.out.println(s);
        }
        jedis.close();
    }

    @Test
    public void stringOperate() {
        //获取redis的客户端
        Jedis jedis = jedisPool.getResource();
        //设置string类型的值
        jedis.set("jedisKey", "jedisValue");
        //获取值
        String jedisKey = jedis.get("jedisKey");
        System.out.println(jedisKey);
        //计数器
        jedis.incr("jincr");
        String jincr = jedis.get("jincr");
        System.out.println(jincr);

        jedis.close();
    }

    @AfterTest
    public void jedisPoolClose() {
        jedisPool.close();
    }

    @Test
    public void testHash() {
        final Jedis jedis = jedisPool.getResource();
        jedis.hset("key1", "field1", "value1");
        final String hget = jedis.hget("key1", "field1");
        final Map<String, String> key1 = jedis.hgetAll("key1");
        for (String s : key1.keySet()) {
            System.out.println("key的值为" + s);
            System.out.println(key1.get(s));
        }
        System.out.println(hget);
        jedis.close();
    }

    @Test
    public void testList() {
        final Jedis jedis = jedisPool.getResource();
        jedis.lpush("listkey", "1", "2", "3");
        final List<String> result = jedis.lrange("listkey", 0, -1);
        for (String s : result) {
            System.out.println(s);
        }
    }

}
