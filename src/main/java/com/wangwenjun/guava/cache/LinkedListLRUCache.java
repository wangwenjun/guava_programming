package com.wangwenjun.guava.cache;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/***************************************
 * @author:Alex Wang
 * @Date:2017/11/13
 * QQ: 532500648
 * QQ群:463962286
 ***************************************/

/**
 * this class is not thread-safe class
 *
 * @param <K>
 * @param <V>
 */
public class LinkedListLRUCache<K, V> implements LRUCache<K, V>
{
    private final int limit;

    private final LinkedList<K> keys = new LinkedList<>();

    private final Map<K, V> cache = new HashMap<>();

    public LinkedListLRUCache(int limit)
    {
        this.limit = limit;
    }

    @Override
    public void put(K key, V value)
    {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        if (keys.size() >= limit)
        {
            K oldestKey = keys.removeFirst();
            cache.remove(oldestKey);
        }

        keys.addLast(key);
        cache.put(key, value);
    }

    @Override
    public V get(K key)
    {
        boolean exist = keys.remove(key);
        if (!exist)
            return null;

        keys.addLast(key);
        return cache.get(key);
    }

    @Override
    public void remove(K key)
    {
        boolean exist = keys.remove(key);
        if (exist)
        {
            cache.remove(key);
        }
    }

    @Override
    public int size()
    {
        return keys.size();
    }

    @Override
    public void clear()
    {
        this.keys.clear();
        this.cache.clear();
    }

    @Override
    public int limit()
    {
        return this.limit;
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        for (K k : keys)
        {
            builder.append(k).append("=").append(cache.get(k)).append(";");
        }
        return builder.toString();
    }
}
