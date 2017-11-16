package com.wangwenjun.guava.cache;

import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang
 * @Date:2017/11/16
 * QQ: 532500648
 * QQ群:463962286
 ***************************************/
public class LRUCacheExample
{

    public static void main(String[] args) throws InterruptedException
    {
        final SoftLRUCache<String, byte[]> cache = new SoftLRUCache<>(100);

        for (int i = 0; i < 1000; i++)
        {
            cache.put(String.valueOf(i), new byte[1024 * 1024 * 2]);
            TimeUnit.MILLISECONDS.sleep(600);
            System.out.println("The " + i + " entity is cached.");
        }
    }
}
