package com.zidane.client.handler;

import com.zidane.client.core.DefaultFuture;
import com.zidane.client.api.Response;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端，对netty通道（channel）接受到的信息进行处理
 *
 * @author Zidane
 * @since 2019-08-22
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 服务器端关闭当前连接的标识
     */
    private static final String SERVER_CONNECT_CLOSE = "1";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
        // 如果收到断开连接的code，则关闭当前channel连接
        if (SERVER_CONNECT_CLOSE.equals(response.getCode())) {
            ctx.channel().close();
        }
        DefaultFuture.receive(response);
    }
}