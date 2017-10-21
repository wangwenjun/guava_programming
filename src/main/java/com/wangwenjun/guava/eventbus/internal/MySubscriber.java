package com.wangwenjun.guava.eventbus.internal;

import java.lang.reflect.Method;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/21
 * 532500648
 ***************************************/
public class MySubscriber
{

    private final Object subscribeObject;

    private final Method subscribeMethod;

    private boolean disable = false;

    public MySubscriber(Object subscribeObject, Method subscribeMethod)
    {
        this.subscribeObject = subscribeObject;
        this.subscribeMethod = subscribeMethod;
    }

    public Object getSubscribeObject()
    {
        return subscribeObject;
    }

    public Method getSubscribeMethod()
    {
        return subscribeMethod;
    }

    public boolean isDisable()
    {
        return disable;
    }

    public void setDisable(boolean disable)
    {
        this.disable = disable;
    }
}
