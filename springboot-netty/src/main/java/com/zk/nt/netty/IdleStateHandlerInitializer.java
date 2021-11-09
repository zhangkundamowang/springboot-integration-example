package com.zk.nt.netty;

import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class IdleStateHandlerInitializer extends ChannelInboundHandlerAdapter {
	
     private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
             Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8));
     
     @Override
     public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
         if(evt instanceof IdleStateEvent) {
			 ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);    // 关闭连接
         } else {
             // 传递给下一个处理程序
             super.userEventTriggered(ctx, evt);
         }
     }

}
