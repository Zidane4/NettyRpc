package com.zidane.controller;

import com.zidane.service.BasicService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 第三方调用测试类
 *
 * @author Zidane
 * @since 2019-08-31
 */
@Configuration
@ComponentScan("com.zidane")  // 不仅会扫描本工程，还会自动扫描所依赖的consumer工程
public class BasicController {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(BasicController.class);
        BasicService basicService = context.getBean(BasicService.class);
        basicService.testSaveUser();
    }
}