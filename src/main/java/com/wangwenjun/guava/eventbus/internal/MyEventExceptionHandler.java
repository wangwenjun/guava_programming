package com.wangwenjun.guava.eventbus.internal;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/21
 * 532500648
 ***************************************/
public interface MyEventExceptionHandler
{
    void handle(Throwable cause, MyEventContext context);
}
