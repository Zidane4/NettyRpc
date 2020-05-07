# 系统整体介绍
基于Netty + ZooKeeper + Fastjson实现的轻量级RPC框架，包含TCP拆包与粘包、fastjson序列化、心跳检测等基本功能。

## 业务简介
本系统是一个利用原生netty能力实现的轻量级RPC框架，利用fastjson进行序列化、实现了心跳检测机制、并利用DelimiterBasedFrameDecoder实现了以分隔符为码流结束标识的消息解码方式，以此解决TCP拆包与粘包问题。

具体的使用方式：
* 下载安装并启动zookeeper。 下载路径：https://zookeeper.apache.org/releases.html      当前系统使用版本：zookeeper-3.4.13
* 启动netty-producer工程下的NettyServer，即启动服务端。
* 执行netty-basic工程下的BasicController，调用rpc的客户端，客户端netty-consumer会再调用服务端，从而完成远程服务的调用。

## 工程模块
```
├── README.md
├── pom.xml
├── netty-api--------系统接口定义
├── netty-consumer---客户端
├── netty-producer---服务端
└── netty-basic------第三方服务调用方