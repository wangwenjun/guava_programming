package com.wangwenjun.servant;

import com.wangwenjun.service.Service;

import java.util.ServiceLoader;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/11
 * @QQ: 532500648
 ***************************************/
public class ServiceInvoker {
    public static void main(String[] args) {
        ServiceLoader<Service> serviceLoader = ServiceLoader.load(Service.class);
        for (Service service : serviceLoader) {
            service.show();
        }
    }
}
