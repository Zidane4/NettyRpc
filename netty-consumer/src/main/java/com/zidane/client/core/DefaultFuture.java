package com.zidane.client.core;

import com.zidane.client.api.ClientRequest;
import com.zidane.client.api.Response;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 每个DefaultFuture对应一个请求，其中request和response是通过id字段判定对应关系的
 * 此处用了锁的机制，保证并发时请求和响应的一致性
 *
 * @author Zidane
 * @since 2019-08-26
 */
public class DefaultFuture {

    public final static ConcurrentHashMap<Long, DefaultFuture> ALL_DEFAULT_FUTRUE
            = new ConcurrentHashMap<Long, DefaultFuture>();

    final Lock lock = new ReentrantLock();

    public Condition condition = lock.newCondition();

    private Response response;

    public DefaultFuture(ClientRequest request) {
        ALL_DEFAULT_FUTRUE.put(request.getId(), this);
    }

    /**
     * 利用锁机制，阻塞等待获取本future对应的响应
     *
     * @return Response  服务端响应
     */
    public Response get() {
        lock.lock();
        try {
            while (!done()) {
                condition.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return response;
    }

    public static void receive(Response response) {
        DefaultFuture df = ALL_DEFAULT_FUTRUE.get(response.getId());
        if (df != null) {
            Lock lock = df.lock;
            lock.lock();
            try {
                df.setResponse(response);
                df.condition.signal();
                ALL_DEFAULT_FUTRUE.remove(df);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private boolean done() {
        if (response != null) {
            return true;
        }
        return false;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}