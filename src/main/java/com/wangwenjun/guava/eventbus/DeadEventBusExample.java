package com.wangwenjun.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.wangwenjun.guava.eventbus.listeners.DeadEventListener;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/19
 * 532500648
 ***************************************/
public class DeadEventBusExample
{

    public static void main(String[] args)
    {

        final DeadEventListener deadEventListener = new DeadEventListener();
        final EventBus eventBus = new EventBus("DeadEventBus")
        {
            @Override
            public String toString()
            {
                return "DEAD-EVENT-BUS";
            }
        };
        eventBus.register(deadEventListener);
        eventBus.post("Hello");
    }
}
