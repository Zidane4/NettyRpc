package com.zidane.client;

import com.alibaba.fastjson.JSONObject;
import com.zidane.client.api.ClientRequest;
import com.zidane.client.api.Response;
import com.zidane.client.core.DefaultFuture;
import com.zidane.client.handler.SimpleClientHandler;
import com.zidane.client.zk.ZookeeperFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;

/**
 * netty的客户端配置类
 *
 * @author Zidane
 * @since 2019-08-26
 */
public class NettyClient {

    /**
     * netty客户端启动引擎
     */
    public static final Bootstrap b = new Bootstrap();

    /**
     * zk上发布的服务地址
     */
    private static final String SERVER_PATH = "/netty";

    /**
     * netty客户端Channel的线程管理实例
     */
    private static ChannelFuture f;

    static {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        b.group(workerGroup);
        b.option(ChannelOption.SO_KEEPALIVE, true)  // 通信方式为长连接
                .channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                // 使用分隔符解码器，分隔符为"\r\n"
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                // 根据UTF-8格式进行解码
                ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                // 设置本系统定制的handler
                ch.pipeline().addLast(new SimpleClientHandler());
                // 根据UTF-8格式进行编码
                ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
            }
        });

        CuratorFramework client = ZookeeperFactory.create();
        // 加zk监听，监听zk上发布的服务变化
        try {
            List<String> serverPaths = client.getChildren().forPath(SERVER_PATH); // 获取zk上的服务列表
            // 获取zk服务列表中的服务，创建channel连接并交给ChannelFuture进行管理（本系统默认服务端只提供一个服务）
            if (!serverPaths.isEmpty()) {
                String[] str = serverPaths.get(0).split("#");
                f = b.connect(str[0], Integer.valueOf(str[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Response send(ClientRequest request) {
        f.channel().writeAndFlush(JSONObject.toJSONString(request));
        f.channel().writeAndFlush(Delimiters.lineDelimiter()[0]);

        // 一次服务调用对应起一个DefaultFutrue（根据id对应）
        DefaultFuture df = new DefaultFuture(request);

        // 此处会阻塞线程，直到接收到从服务端返回的消息
        return df.get();
    }
}