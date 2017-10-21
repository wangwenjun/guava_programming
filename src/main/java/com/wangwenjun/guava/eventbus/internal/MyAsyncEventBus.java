package com.wangwenjun.guava.eventbus.internal;

import java.util.concurrent.ThreadPoolExecutor;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/21
 * 532500648
 ***************************************/
public class MyAsyncEventBus extends MyEventBus
{

    public MyAsyncEventBus(String busName, MyEventExceptionHandler exceptionHandler, ThreadPoolExecutor executor)
    {
        super(busName, exceptionHandler, executor);
    }


    public MyAsyncEventBus(String busName, ThreadPoolExecutor executor)
    {
        this(busName, null, executor);
    }

    public MyAsyncEventBus(ThreadPoolExecutor executor)
    {
        this("default-async", null, executor);
    }

    public MyAsyncEventBus(MyEventExceptionHandler exceptionHandler, ThreadPoolExecutor executor)
    {
        this("default-async", exceptionHandler, executor);
    }
}
