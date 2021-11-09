package com.zk.nt.netty;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * NettyServer Netty服务器配置
 * @author
 * @date
 */
public class NettyServer {
    
    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);


    private static final ServerHandler serverHandler = new ServerHandler();
    //netty监听端口
    private static Integer port = 5196;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup group = new NioEventLoopGroup();
    private ChannelFuture cf;
    
    public NettyServer(int port) {
        this.port = port;
    }
 
    public void start() throws Exception {
        bossGroup = new NioEventLoopGroup();
        group = new NioEventLoopGroup();
        ServerBootstrap sb = new ServerBootstrap();
        sb.option(ChannelOption.SO_BACKLOG, 20);
        sb.group(group, bossGroup) // 绑定线程池
                .channel(NioServerSocketChannel.class) // 指定使用的channel
                .localAddress(this.port)// 绑定监听端口
                .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        logger.info("收到新的客户端连接: {}",ch.toString());
                        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                        ch.pipeline().addLast(new HttpServerCodec());
                        //心跳检测,参数说明：[长时间未写:长时间未读:长时间未读写:时间单位]~[读写是对连接本生而言，写：未向服务端发送消息，读：未收到服务端的消息]
                        ch.pipeline().addLast(new IdleStateHandler(0,5*60,0, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new IdleStateHandlerInitializer());
                        //以块的方式来写的处理器
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        ch.pipeline().addLast(new HttpObjectAggregator(8192));
                        ch.pipeline().addLast(serverHandler);
                        //最后一个参数为数据包大小
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536 * 10));
                    }
                });
        cf = sb.bind().sync(); // 服务器异步创建绑定
        logger.info(NettyServer.class + " 启动正在监听： " + cf.channel().localAddress());
    }
    
    public void destroy() {
    	logger.info(NettyServer.class + " netty服务监听关闭： " + cf.channel().localAddress());
    	try {
    		cf.channel().closeFuture().sync();// 关闭服务器通道
    	} catch (Exception e) {
    		e.printStackTrace();
    	}finally{   		
    		bossGroup.shutdownGracefully().syncUninterruptibly();
    		group.shutdownGracefully().syncUninterruptibly();
    	}

    }
}
