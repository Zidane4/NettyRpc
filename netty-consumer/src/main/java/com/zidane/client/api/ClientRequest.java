package com.zidane.client.api;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 客户端请求
 *
 * @author Zidane
 * @since 2019-08-26
 */
public class ClientRequest {
    private final long id;

    private Object content;

    private static final AtomicLong aid = new AtomicLong(1);

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ClientRequest() {
        id = aid.incrementAndGet();
    }

    public long getId() {
        return id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public AtomicLong getAid() {
        return aid;
    }
}