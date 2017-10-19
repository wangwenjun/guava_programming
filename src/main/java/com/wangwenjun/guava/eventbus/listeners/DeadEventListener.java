package com.wangwenjun.guava.eventbus.listeners;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/19
 * 532500648
 ***************************************/
public class DeadEventListener
{
    @Subscribe
    public void handle(DeadEvent event)
    {
        System.out.println(event.getSource());
        System.out.println(event.getEvent());
    }
}
