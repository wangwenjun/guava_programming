package com.wangwenjun.guava.eventbus.events;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/19
 * 532500648
 ***************************************/
public class Request
{

    private final String orderNo;

    public Request(String orderNo)
    {
        this.orderNo = orderNo;
    }

    @Override
    public String toString()
    {
        return "Request{" +
                "orderNo='" + orderNo + '\'' +
                '}';
    }
}
