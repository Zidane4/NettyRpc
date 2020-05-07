package com.zidane.service;

import com.zidane.client.annotation.Remote;
import com.zidane.user.model.User;
import com.zidane.user.remote.UserRemote;

import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Service;

/**
 * 第三方调用服务测试类
 *
 * @author Zidane
 * @since 2019-08-31
 */
@Service
public class BasicService {
    @Remote
    private UserRemote userRemote;

    public void testSaveUser() {
        User user = new User();
        user.setId(1);
        user.setMessage("testUserName");
        Object reponse = userRemote.saveUser(user);
        System.out.println(JSONObject.toJSONString(reponse));
    }
}