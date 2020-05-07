package com.zidane.netty.medium;

import com.zidane.netty.annotation.Remote;

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 将服务器端对外提供的服务（含Remote注解）的信息存入到Media.beanMap（本地服务缓存）
 *
 * @author Zidane
 * @since 2019-08-26
 */
@Component
public class InitialMedium implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Remote.class)) {
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method m : methods) {
                String key = bean.getClass().getInterfaces()[0].getName() + "." + m.getName();
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setMethod(m);

                // 将服务名和对应的bean存入beanMap
                Media.beanMap.put(key, beanMethod);
            }
        }
        return bean;
    }
}