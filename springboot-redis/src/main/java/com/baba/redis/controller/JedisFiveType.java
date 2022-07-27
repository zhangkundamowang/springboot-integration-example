package com.baba.redis.controller;

import com.baba.redis.util.RedisUtil;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JedisFiveType {

    private Jedis jedis;

    @Before
    public void setUp() {
        jedis = new Jedis("192.168.56.3", 6379,0);
        jedis.auth("root");
        System.out.println(jedis.ping());
    }

    /**
     * redis存储字符串
     */
    @Test
    public void testString() {
        jedis.set("name", "zhangkun");
        System.out.println(jedis.get("name"));
        //拼接
        jedis.append("name", " is cool");
        System.out.println(jedis.get("name"));
        //删除
        jedis.del("name");
        System.out.println(jedis.get("name"));
        //设置多个键值对
        jedis.mset("name", "zhangkun", "age", "24", "qq", "110");
        //进行加1操作
        jedis.incr("age");
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
        //关闭jedis
        jedis.close();
    }

    /**
     * redis操作Map
     */
    @Test
    public void testMap() {
        //添加数据
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "zhangkun");
        map.put("age", "22");
        map.put("qq", "123456");
        jedis.hmset("user", map);
        //取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> rsmap = jedis.hmget("user", "name", "age", "qq");
        System.out.println(rsmap);

        //删除map中的某个键值
        jedis.hdel("user", "age");
        System.out.println(jedis.hmget("user", "age")); //因为删除了，所以返回的是null
        System.out.println(jedis.hlen("user")); //返回key为user的键中所存放的值的个数2
        System.out.println(jedis.exists("user"));//是否存在key为user的记录 返回true
        System.out.println(jedis.hkeys("user"));//返回map对象中的所有key
        System.out.println(jedis.hvals("user"));//返回map对象中的所有value

        Iterator<String> iter = jedis.hkeys("user").iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + jedis.hmget("user", key));
        }
    }

    /**
     * jedis操作List
     * 1.lpush 从左往右添加元素
     * 在key 对应 list的头部添加字符串元素
     * 2.rpush
     * 从右到左添加元素
     * 在key 对应 list 的尾部添加字符串元素
     */
    @Test
    public void testList() {
        //开始前，先移除所有的内容
        jedis.del("java framework");
        System.out.println(jedis.lrange("java framework", 0, -1));
        //先向key java framework中存放三条数据
        jedis.lpush("java framework", "spring");
        jedis.lpush("java framework", "struts");
        jedis.lpush("java framework", "hibernate");
        //再取出所有数据jedis.lrange是按范围取出，
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        System.out.println(jedis.lrange("java framework", 0, -1));

        jedis.del("java framework");
        jedis.rpush("java framework", "spring");
        jedis.rpush("java framework", "struts");
        jedis.rpush("java framework", "hibernate");
        System.out.println(jedis.lrange("java framework", 0, -1));
    }

    /**
     * jedis操作Set
     */
    @Test
    public void testSet() {
        jedis.flushAll();
        //添加
        jedis.sadd("user", "zs");
        jedis.sadd("user", "ls");
        jedis.sadd("user", "ww");
        jedis.sadd("user", "gl");
        jedis.sadd("user", "tq");
        //移除noname
        jedis.srem("user", "tq");
        System.out.println(jedis.smembers("user"));//获取所有加入的value
        System.out.println(jedis.sismember("user", "tq"));//判断 who 是否是user集合的元素
        System.out.println(jedis.srandmember("user"));//返回集合中的一个随机元素
        System.out.println(jedis.scard("user"));//返回集合的元素个数
    }

    @Test
    public void test() throws InterruptedException {
        //jedis 排序
        //注意，此处的rpush和lpush是List的操作。是一个双向链表（单从表现来看的）
        jedis.del("a");//先清除数据，再加入数据进行测试
        jedis.rpush("a", "1");
        jedis.lpush("a", "6");
        jedis.lpush("a", "3");
        jedis.lpush("a", "9");
        System.out.println(jedis.lrange("a", 0, -1));//未排序
        System.out.println(jedis.sort("a")); //输入排序后结果
    }

    /**
     * 测试RedisUtil
     */
    @Test
    public void testRedisPool() {
        RedisUtil.getJedis().set("newName", "中文测试");
        System.out.println(RedisUtil.getJedis().get("newName"));

        RedisUtil.set("UtilTest", "success");
        System.out.println(RedisUtil.getJedis().get("UtilTest"));
        System.out.println(RedisUtil.getStr("UtilTest"));
    }

    /**
     * Redis事务
     */
    @Test
    public void testTransaction(){
            int balance;                //可用余额
            int debt;                   //欠费
            int amtToSubtract = 10;     //实刷额度

            jedis.watch("balance");
            balance = Integer.parseInt(jedis.get("balance"));
            //如果可用余额小于实刷额度
            if (balance < amtToSubtract) {
                jedis.unwatch();
            } else {
                System.out.println("******开启事务******");
                Transaction transaction=jedis.multi();
                transaction.decrBy("balance",amtToSubtract);
                transaction.incrBy("debt",amtToSubtract);
                transaction.exec();
                balance = Integer.parseInt(jedis.get("balance"));
                debt = Integer.parseInt(jedis.get("debt"));

                System.out.println("可用余额" + balance);
                System.out.println("欠费" + debt);
            }
        }

    @Test
    public void testTransactionException() {
        try {
            Transaction transaction = jedis.multi();
            transaction.lpush("key", "11");
            transaction.lpush("key", "22");
            int a = 6 / 0;
            transaction.lpush("key", "33");
            List<Object> list = transaction.exec();
            System.out.println(jedis.lrange("key",0,-1));
        } catch (Exception e) {
            System.out.println("异常=====");
        }
    }

    @Test
    public void testMiaoShaException() {
        try {

        } catch (Exception e) {
            System.out.println("异常=====");
        }
    }

}
