package com.zidane.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.zidane.netty.constant.Constants;
import com.zidane.netty.api.ServerRequest;
import com.zidane.netty.medium.Media;
import com.zidane.netty.api.Response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 服务器端，对netty通道（channel）接受到的信息进行处理
 *
 * @author Zidane
 * @since 2019-08-22
 */
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取请求信息
        ServerRequest request = JSONObject.parseObject(msg.toString(), ServerRequest.class);

        // 根据名称调用对应服务
        Response response = Media.newInstance().process(request);

        // 通过Channel发送数据
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
        ctx.channel().writeAndFlush(Delimiters.lineDelimiter()[0]);

        // 根据标识符判定是否关闭连接
        if (Constants.SERVER_CONNECT_CLOSE.equals(response.getCode())) {
            ctx.channel().close();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 对于通道channel超时，进行对应的处理
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                System.out.println("Read idle timeout.");
                ctx.channel().close();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                System.out.println("Write idle timeout.");
                ctx.channel().close();
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("Read and Write idle timeout.");
                ctx.channel().close();
            }
        }
    }
}