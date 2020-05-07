package com.zidane.user.remote;

import com.zidane.user.model.User;

/**
 * 服务声明
 *
 * @author Zidane
 * @since 2019-08-27
 */
public interface UserRemote {
    Object saveUser(User user);
}