package com.wangwenjun.guava.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.wangwenjun.guava.eventbus.listeners.SimpleListener;

import java.util.concurrent.Executors;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/21
 * 532500648
 ***************************************/
public class AsyncEventBusExample
{
    public static void main(String[] args)
    {
        AsyncEventBus eventBus = new AsyncEventBus(Executors.newFixedThreadPool(4));
        eventBus.register(new SimpleListener());
        eventBus.post("hello");

    }
}
