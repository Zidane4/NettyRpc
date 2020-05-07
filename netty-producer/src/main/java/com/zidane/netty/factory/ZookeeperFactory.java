package com.zidane.netty.factory;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * zk工厂方法，此处用于建立与zk的连接
 *
 * @author Zidane
 * @since 2019-08-22
 */
public class ZookeeperFactory {
    public static CuratorFramework client;

    public static CuratorFramework create() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        return client;
    }
}