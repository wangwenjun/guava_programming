package com.wangwenjun.guava.eventbus.internal;

import java.lang.reflect.Method;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/21
 * 532500648
 ***************************************/
public interface MyEventContext
{

    String getSource();

    Object getSubscriber();

    Method getSubscribe();

    Object getEvent();
}
