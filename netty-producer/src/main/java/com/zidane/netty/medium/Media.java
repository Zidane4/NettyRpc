package com.zidane.netty.medium;

import com.zidane.netty.api.ServerRequest;
import com.zidane.netty.api.Response;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据客户端的请求，调用对应的服务端服务，进行处理
 *
 * @author Zidane
 * @since 2019-08-26
 */
public class Media {
    public static Map<String, BeanMethod> beanMap;

    static {
        beanMap = new HashMap<String, BeanMethod>();
    }

    private static Media m = new Media();

    private Media() {

    }

    public static Media newInstance() {
        return m;
    }

    /**
     * 根据客户端请求，获取具体的服务，最后通过反射进行业务处理
     *
     * @param request 客户端请求
     * @return Response  服务端响应
     */
    public Response process(ServerRequest request) {
        Response result = null;
        try {
            String command = request.getCommand();
            BeanMethod beanMethod = beanMap.get(command);
            if (beanMethod == null) {
                return null;
            }

            Object bean = beanMethod.getBean();
            Method method = beanMethod.getMethod();
            Class paramType = method.getParameterTypes()[0];
            Object args = JSONObject.parseObject(JSONObject.toJSONString(request.getContent()), paramType);
            result = (Response) method.invoke(bean, args);
            result.setId(request.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}