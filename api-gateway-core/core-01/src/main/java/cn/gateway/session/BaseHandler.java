package cn.gateway.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, T msg) throws Exception {


        session(channelHandlerContext, channelHandlerContext.channel(), msg);
    }

    protected abstract void session(ChannelHandlerContext ctx, final Channel channel, T request);
}
