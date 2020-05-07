package com.zidane.user.model;

/**
 * 用户Pojo
 *
 * @author Zidane
 * @since 2019-08-26
 */
public class User {
    private Integer id;

    private String message;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", message='" + message + '\'' + '}';
    }
}