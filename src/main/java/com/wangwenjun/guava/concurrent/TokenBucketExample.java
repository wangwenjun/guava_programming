package com.wangwenjun.guava.concurrent;

/***************************************
 * @author:Alex Wang
 * @Date:2017/11/12
 * QQ: 532500648
 * QQ群:463962286
 ***************************************/
public class TokenBucketExample
{

    public static void main(String[] args)
    {
        final TokenBucket tokenBucket = new TokenBucket();
        for (int i = 0; i < 200; i++)
        {
            new Thread(tokenBucket::buy).start();
        }
    }
}
