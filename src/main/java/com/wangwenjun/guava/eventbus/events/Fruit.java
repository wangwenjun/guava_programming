package com.wangwenjun.guava.eventbus.events;

import com.google.common.base.MoreObjects;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/18
 * 532500648
 ***************************************/
public class Fruit
{

    private final String name;

    public Fruit(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("Name", name).toString();
    }
}