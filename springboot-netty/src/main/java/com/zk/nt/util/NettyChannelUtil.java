package com.zk.nt.util;

import io.netty.channel.Channel;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NettyChannelUtil {

    //活跃通道列表  channelId:channel
    public static ConcurrentHashMap<String, Channel> sessionChannelMap = new ConcurrentHashMap<String, Channel>();
    //桩编号和通道id映射  pileCode:channelId
    public static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
    //保存连接时间   channelId:time
    public static ConcurrentHashMap<String, String> mapTime = new ConcurrentHashMap<String, String>();

    /**
     * 获取所有连接
     */
    public static Map<String, Channel> channelAll() {
        return sessionChannelMap;
    }

    /**
     * 获取所有连接的时间
     */
    public static Map<String, String> channelConnectTimeAll() {
        return mapTime;
    }

    /**
     * 获取连接时间
     */
    public static String getTime(String channelId) {
        return mapTime.get(channelId);
    }

    /**
     * 获取连接对应的桩编号
     */
    public static String getPileCode(String channelId) {
        return map.get(channelId);
    }

    /**
     * 获取连接数
     */
    public static Integer getConnectNumber() {
        return sessionChannelMap.size();
    }

    /**
     * 添加连接
     */
    public static boolean addConnect(Channel channel) {
        String channelId = channel.id().asLongText();
        //作为主动请求的依据,若是重连会覆盖
        channelAll().put(channelId, channel);
        channelConnectTimeAll().put(channelId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return true;
    }

    /**
     * 移除连接
     */
    public static boolean deleteConnect(Channel channel) {
        String channelId = channel.id().asLongText();
        channelAll().remove(channelId);
        channelConnectTimeAll().remove(channelId);
        return true;
    }

    /**
     * @return java.util.List<java.lang.String>
     * @Author xiongchuan
     * @Description 根据value获取值
     * @Date 2020/5/7 23:44
     * @Param [value]
     **/
    public static List<String> getByValue(String value) {
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            if (map.get(key).equals(value)) {
                list.add(key);
            }
        }
        return list;
    }

}
