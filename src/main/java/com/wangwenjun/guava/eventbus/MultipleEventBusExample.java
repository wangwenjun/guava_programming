package com.wangwenjun.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.wangwenjun.guava.eventbus.listeners.MultipleEventListeners;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/18
 * 532500648
 ***************************************/
public class MultipleEventBusExample
{
    public static void main(String[] args)
    {
        final EventBus eventBus = new EventBus();
        eventBus.register(new MultipleEventListeners());
        System.out.println("post the string event");
        eventBus.post("I am string event");
        System.out.println("post the int event");
        eventBus.post(1000);

    }
}
