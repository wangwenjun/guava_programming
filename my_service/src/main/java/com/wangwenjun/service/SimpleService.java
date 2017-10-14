package com.wangwenjun.service;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/11
 * @QQ: 532500648
 ***************************************/
public class SimpleService implements Service {
    @Override
    public void show() {
        System.out.println("hi i come from the service loader.");
    }
}
