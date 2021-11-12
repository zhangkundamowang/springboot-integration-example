package com.zk.nt.server;

import com.zk.nt.MyByteToLongDecoder;
import com.zk.nt.MyLongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        /* 向管道加入处理器 */
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 入站的 handler 进行解码
        pipeline.addLast("decoder", new MyByteToLongDecoder());
        // 添加一个出站的 handler 对数据进行编码
        pipeline.addLast("encoder", new MyLongToByteEncoder());

        // 添加自定义的处理器
        pipeline.addLast("MyServerHandler", new MyServerHandler());

    }

}