package com.zidane.netty;

import com.zidane.netty.constant.Constants;
import com.zidane.netty.factory.ZookeeperFactory;
import com.zidane.netty.handler.SimpleServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 服务端配置启动类
 *
 * @author Zidane
 * @since 2019-08-22
 */
@Configuration
@ComponentScan("com.zidane")
public class NettyServer {
    public static void main(String args[]) {
        ApplicationContext context = new AnnotationConfigApplicationContext(NettyServer.class);
        init();
    }

    private static void init() {
        // 轮询处理客户端连接的线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理Channel（通道）I/O事件的线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);

            bootstrap.option(ChannelOption.SO_BACKLOG, 128)         // 请求队列的大小
                    .childOption(ChannelOption.SO_KEEPALIVE, false)  // 设置通信方式为长连接
                    .channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 使用分隔符解码器，分隔符为"\r\n"
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
                    // 根据UTF-8格式进行解码
                    ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    // 服务端添加IdleStateHandler心跳检测处理器
                    ch.pipeline()
                            .addLast(new IdleStateHandler(Constants.SERVER_CHANNEL_READERIDLETIME,
                                    Constants.SERVER_CHANNEL_WRITERIDLETIME, 0, TimeUnit.MINUTES));
                    // 设置本系统定制的handler
                    ch.pipeline().addLast(new SimpleServerHandler());
                    // 根据UTF-8格式进行编码
                    ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
                }
            });

            // 启动一个netty服务
            ChannelFuture f = bootstrap.bind(Constants.SERVER_PORT).sync();
            CuratorFramework client = ZookeeperFactory.create();
            InetAddress inetAddress = InetAddress.getLocalHost();
            // 将netty服务注册到zk客户端
            client.create()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(Constants.SERVER_PATH + inetAddress.getHostAddress() + "#" + Constants.SERVER_PORT + "#");
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}