package com.baba.nt.client;

import com.baba.nt.MyByteToLongDecoder;
import com.baba.nt.MyLongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 入站的 handler 进行解码
        pipeline.addLast("decoder", new MyByteToLongDecoder());
        // 添加一个出站的 handler 对数据进行编码
        pipeline.addLast("encoder", new MyLongToByteEncoder());

        // 添加自定义 handler，处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }
}