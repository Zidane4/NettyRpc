package com.zidane.netty.business;

import com.zidane.netty.annotation.Remote;
import com.zidane.netty.constant.Constants;
import com.zidane.netty.api.Response;
import com.zidane.user.model.User;
import com.zidane.user.remote.UserRemote;

/**
 * 业务服务实现类
 *
 * @author Zidane
 * @since 2020-05-06
 */
@Remote
public class UserRemoteImpl implements UserRemote {
    @Override
    public Response saveUser(User user) {
        Response response = new Response();
        if ("exit".equals(user.getMessage())) {
            response.setResult("Close the connection immediately.");
            response.setCode(Constants.SERVER_CONNECT_CLOSE);
        } else {
            response.setResult("Receive message from " + user.getMessage() + " successed.");
            response.setCode(Constants.SERVER_CONNECT_NORMAL);
        }
        return response;
    }
}