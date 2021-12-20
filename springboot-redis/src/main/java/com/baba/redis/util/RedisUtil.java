package com.baba.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;


/**
 * redis连接池
 */
@Slf4j
public final class RedisUtil {

    //Redis服务器IP
    private static String ADDR = "127.0.0.1";

    //Redis的端口号
    private static int PORT = 6379;

    //访问密码
    private static String AUTH = "";

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_TOTAL = 1024;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT_MILLIS = 10000;
    private static int TIMEOUT = 10000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;

    /**
     * 静态块，初始化Redis连接池
     */
    private static void initializePool() {
        try {
            log.info("ADDR:" + ADDR + ";PORT:" + PORT + ";AUTH" + AUTH);
            JedisPoolConfig config = new JedisPoolConfig();
            /*注意：
            在高版本的jedis jar包，比如本版本2.9.0，JedisPoolConfig没有setMaxActive和setMaxWait属性了
            这是因为高版本中官方废弃了此方法，用以下两个属性替换。
            maxActive  ==>  maxTotal
            maxWait==>  maxWaitMillis
            */
            //设置最大连接数（100个足够用了，没必要设置太大）
            config.setMinIdle(2);
            config.setMaxTotal(MAX_TOTAL);
            //最大空闲连接数  原因，运行环境配置较差，redis连接数过高所致，降低redis.maxIdle和redis.maxActive后，问题解决。没有再出现。
            config.setMaxIdle(MAX_IDLE);
            //获取Jedis连接的最大等待时间（50秒）
            config.setMaxWaitMillis(MAX_WAIT_MILLIS);
            //在获取Jedis连接时，自动检验连接是否可用
            config.setTestOnBorrow(TEST_ON_BORROW);
            //在将连接放回池中前，自动检验连接是否有效
            config.setTestOnReturn(true);
            //自动测试池中的空闲连接是否都是可用连接
            config.setTestWhileIdle(true);
            //密码为空就不认证
            if (StringUtils.isBlank(AUTH)) {
                jedisPool = new JedisPool(config, ADDR, PORT);
            } else {
                jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     */
    public static Jedis getJedis() {
        if (null == jedisPool) {
            poolInit();
        }
        Jedis jedis = null;
        int timeoutCount = 0;
        int retryNum = 3;
        while (jedis == null && timeoutCount < retryNum) {
            try {
                jedis = jedisPool.getResource();
            } catch (Exception e) {
                if (e instanceof JedisConnectionException) {
                    timeoutCount++;
                    log.info("getJedis timeoutCount={}", timeoutCount);
                    if (timeoutCount > 3) {
                        break;
                    }
                } else {
                    log.info("jedisInfo ... NumActive=" + jedisPool.getNumActive()
                            + ", NumIdle=" + jedisPool.getNumIdle()
                            + ", NumWaiters=" + jedisPool.getNumWaiters()
                            + ", isClosed=" + jedisPool.isClosed());
                    log.info("GetJedis error,{}", e);
                    break;
                }
            }
            timeoutCount++;
        }
        return jedis;
    }

    /**
     * 多线程环境同步初始化（保证项目中有且仅有一个连接池）
     */
    private static synchronized void poolInit() {
        if (null == jedisPool) {
            initializePool();
        }
    }

    // 设置过期时间
    public static boolean setTime(String key, String value, Integer expireSecond) {
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            jedis.setex(key, expireSecond, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeJedisPool(jedis);
        }
    }

    // 不设置过期时间
    public static boolean set(String key, String value) {
        Jedis jedis;
        jedis = RedisUtil.getJedis();
        try {
            boolean keyExist = jedis.exists(key);
            if (keyExist) {
                jedis.del(key);
            }
            jedis.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeJedisPool(jedis);
        }
    }

    // 删除缓存
    public static boolean del(String key) {
        Jedis jedis = RedisUtil.getJedis();
        try {
            jedis.del(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeJedisPool(jedis);
        }
    }

    // 取数据
    public static String getStr(String key) {
        Jedis jedis = null;
        String data = null;
        try {
            jedis = RedisUtil.getJedis();
            data = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeJedisPool(jedis);
        }
        return data;
    }

    public static void closeJedisPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close(); //注意这里不是关闭连接，在JedisPool模式下，Jedis会被归还给资源池。
        }
    }

    /**
     * 释放jedis资源
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

}