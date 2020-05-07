package com.zidane.client.proxy;

import com.zidane.client.annotation.Remote;
import com.zidane.client.NettyClient;
import com.zidane.client.api.ClientRequest;
import com.zidane.client.api.Response;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * 利用spring框架，对指定服务进行拦截处理，将服务调用转化为netty可以传送的请求，然后发送给服务器
 *
 * @author Zidane
 * @since 2019-08-28
 */
@Component
public class InvokeProxy implements BeanPostProcessor {
    @Override
    @Nullable
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 获取所有被RemoteInvoke注解的属性，然后设置增强
            if (field.isAnnotationPresent(Remote.class)) {
                field.setAccessible(true);
                final Map<Method, Class> methodClassMap = new HashMap<>();
                putMethodClass(methodClassMap, field);
                // 使用CgLib动态代理方式创建代理类
                Enhancer enhancer = new Enhancer();
                enhancer.setInterfaces(new Class[] {field.getType()});
                enhancer.setCallback(new MethodInterceptor() {
                    // MethodInterceptor: 代理类的所有方法调用都会转而执行这个接口中的intercept方法而不是原方法
                    @Override
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy)
                            throws Throwable {
                        // 将用户的请求信息，按照本系统定义的协议进行包装（包装成统一的ClientRequest），然后发送给服务器端
                        ClientRequest request = new ClientRequest();
                        request.setCommand(methodClassMap.get(method).getName() + "." + method.getName());
                        request.setContent(args[0]);
                        Response response = NettyClient.send(request);
                        return response;
                    }
                });
                try {
                    field.set(bean, enhancer.create());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    @Nullable
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 将属性（被RemoteInvoke注解的属性）的所有方法和属性对应的接口类型对应存入到Map中
     * 本系统中此属性对应的是UserRemote
     *
     * @param methodClassMap    方法和接口对应的Map
     * @param field             被注解的属性
     */
    private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
        Method[] methods = field.getType().getDeclaredMethods();
        for (Method method : methods) {
            methodClassMap.put(method, field.getType());
        }
    }
}