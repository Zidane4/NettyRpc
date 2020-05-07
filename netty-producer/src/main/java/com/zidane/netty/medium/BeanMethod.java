package com.zidane.netty.medium;

import java.lang.reflect.Method;

/**
 * 服务信息类
 *
 * @author Zidane
 * @since 2019-08-26
 */
public class BeanMethod {
    private Object bean;

    private Method method;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}