package com.zidane.netty.api;

/**
 * 服务请求
 *
 * @author Zidane
 * @since 2019-08-26
 */
public class ServerRequest {
    private Long id;

    private Object content;

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}