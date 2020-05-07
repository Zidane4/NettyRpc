package com.zidane.netty.constant;

/**
 * 系统常量
 *
 * @author Zidane
 * @since 2019-08-22
 */
public class Constants {
    /**
     * 服务注册在zk上的地址
     */
    public static final String SERVER_PATH = "/netty/";

    /**
     * 服务端的服务监听端口
     */
    public static final int SERVER_PORT = 8081;

    /**
     * Channel读空闲超时时间
     */
    public static final long SERVER_CHANNEL_READERIDLETIME = 20;

    /**
     * Channel写空闲超时时间
     */
    public static final long SERVER_CHANNEL_WRITERIDLETIME = 30;

    /**
     * 退出系统的错误码
     */
    public static final String SERVER_CONNECT_CLOSE = "1";

    /**
     * 正常通讯的标识码
     */
    public static final String SERVER_CONNECT_NORMAL = "0";
}