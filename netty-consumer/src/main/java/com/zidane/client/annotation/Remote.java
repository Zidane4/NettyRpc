package com.zidane.client.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 远程调用服务的标签
 *
 * @author Zidane
 * @since 2019-08-28
 */
@Target( {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Remote {
}