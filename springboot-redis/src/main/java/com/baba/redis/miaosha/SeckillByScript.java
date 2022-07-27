package com.baba.redis.miaosha;

import com.baba.redis.util.RedisUtil;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class SeckillByScript {
    static String secKillScript1 = "local userid=KEYS[1];\n" +
            "local prodid=KEYS[2];\n" +
            "local qtkey=\"sk:\"..prodid..\":qt\";\n" +
            "local userskey=\"sk:\"..prodid..\":user\";\n" +
            "local userExists=redis.call(\"sismember\",userskey,userid);" +
            "if tonumber(userExists)==1 then\n" +
            "  return 2;\n" +
            "end\n" +
            "local num=redis.call(\"get\",qtkey);\n" +
            "if tonumber(num)<=0 then\n" +
            "  return 0;\n" +
            "else\n" +
            "  redis.call(\"decr\",qtkey);\n" +
            "  redis.call(\"sadd\",userskey,userid);\n" +
            "  end\n" +
            "  return 1";

    public boolean doSecKill(String userid, String prodid) {
        Jedis jedis = RedisUtil.getJedis();
        String sha1 = jedis.scriptLoad(secKillScript1);
        Object result = jedis.evalsha(sha1, 2, userid, prodid);
        //不关闭连接池会在第二次模拟报错
        jedis.close();
        String reString = String.valueOf(result);
        if ("0".equals(reString)) {
            System.out.println("已抢空！");
            return false;
        } else if ("1".equals(reString)) {
            System.out.println("抢购成功");
            return true;
        } else if ("2".equals(reString)) {
            System.out.println("该用户已抢过！");
            return false;
        } else {
            System.out.println("抢购异常");
            return false;
        }
    }
}
