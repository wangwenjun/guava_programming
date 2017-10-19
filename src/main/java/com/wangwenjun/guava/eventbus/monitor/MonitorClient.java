package com.wangwenjun.guava.eventbus.monitor;

import com.google.common.eventbus.EventBus;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/19
 * 532500648
 ***************************************/

/**
 * tail
 * Apache Flume 1.7 Spooling
 *
 * .position
 *
 */
public class MonitorClient
{
    public static void main(String[] args) throws Exception
    {
        final EventBus eventBus = new EventBus();
        eventBus.register(new FileChangeListener());

        TargetMonitor monitor = new DirectoryTargetMonitor(eventBus, "G:\\Teaching\\汪文君Google Guava实战视频\\monitor");
        monitor.startMonitor();
    }
}

