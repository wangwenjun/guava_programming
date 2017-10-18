package com.wangwenjun.guava.eventbus.listeners;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/18
 * 532500648
 ***************************************/
public class SimpleListener
{
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleListener.class);

    @Subscribe
    public void doAction(final String event)
    {
        if (LOGGER.isInfoEnabled())
        {
            LOGGER.info("Received event [{}] and will take a action", event);
        }
    }
}
