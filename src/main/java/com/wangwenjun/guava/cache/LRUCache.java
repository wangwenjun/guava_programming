package com.wangwenjun.guava.cache;

/***************************************
 * @author:Alex Wang
 * @Date:2017/11/13
 * QQ: 532500648
 * QQ群:463962286
 ***************************************/
public interface LRUCache<K, V>
{

    void put(K key, V value);

    V get(K key);

    void remove(K key);

    int size();

    void clear();

    int limit();
}
