package com.zk.nt.netty;

import cn.hutool.core.thread.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 客户端触发操作
 */
@Service
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

    /**
     * channelAction
     * channel 通道 action 活跃的
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            LOGGER.info("================13. 通道活跃中....========================hashcode值：" + this.hashCode());
        } catch (Exception e) {
            LOGGER.info("未知错误！");
        }
    }

    /**
     * channelInactive
     * channel 通道 Inactive 不活跃的
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOGGER.warn("--------Netty Disconnect Client IP is :" + ctx.channel().id().asShortText() + " " + ctx.channel().remoteAddress() + "--------");
        removeChannel(ctx.channel());
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("================17. Netty读取信息已经完成！========================");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("--------Netty Exception ExceptionCaught :" + ctx.channel().id().asShortText() + " "
                + cause.getMessage() + "=======================\r\n");
        ctx.close();
    }

    /**
     * 检测指定时间内无读写操作时触发，此设置在pipeline链路中设置
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断事件是不是IdleStateEvent事件，然后再判断是否为读空闲or写空闲or读写空闲，是就做相应处理，不是就把事件透传给下一个处理类
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                ctx.close();
                LOGGER.info("================17. 服务器检测到未在指定时间内接收到客户端【" + ctx.channel().id().asShortText() + "】的数据，关闭此通道！\r\n");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 移除通道
     *
     * @param channel
     */
    public void removeChannel(Channel channel) {
        try {
            //去除不活跃的通道映射
            LOGGER.info("--------移除通道");
        } catch (Exception e) {
            LOGGER.info("未知错误！");
        }
    }

    /**
     * 功能：读取服务器发送过来的信息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("================14.5 通道读取服务器发送过来的信息========================");
        try {
            byte req[] = (byte[]) msg;
            String clientId = ctx.channel().id().asLongText();
            /**
             *  使用不同于nioEventLoopGroup的线程池去处理耗时操作，避免阻塞nioEventLoopGroup线程池
             */
            ThreadUtil.execute(() -> {
                System.out.println("读取服务器发送过来的信息==============");
            });
        } catch (Exception e) {
            LOGGER.info("================14.5 通道读取消息异常:{}========================", e.getMessage());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    public static void send(Channel channel, byte[] data) {
        if (data == null) {
            LOGGER.error("通道：" + channel.id().asLongText() + " 发送数据失败，数据为空");
        }
        if (!channel.isActive()) {
            channel.close();
            LOGGER.error("通道：" + channel.id().asLongText() + ":发送数据失败，通道已断开");
            return;
        }
        //write的时候用池化 buf.retain 和 buf.release()要成对出现，避免内存无限增长出现泄漏
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(data.length).writeBytes(data).retain(1);
        channel.eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        if (!future.isSuccess()) {
                            LOGGER.error(Thread.currentThread().getName() + ":发送数据失败");
                        }
                        buf.release();
                    }
                });
            }
        });
    }
}