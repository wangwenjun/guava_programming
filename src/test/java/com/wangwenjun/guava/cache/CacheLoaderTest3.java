package com.wangwenjun.guava.cache;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.cache.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/***************************************
 * @author:Alex Wang
 * @Date:2018/1/9
 * QQ: 532500648
 * QQ群:463962286
 ***************************************/
public class CacheLoaderTest3
{

    @Test
    public void testLoadNullValue() throws ExecutionException
    {
        CacheLoader<String, Employee> loader =
                CacheLoader.from(k -> k.equals("null") ? null : new Employee(k, k, k));
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder().build(loader);
        Employee alex = cache.get("alex");
        assertThat(alex, notNullValue());

        Employee result = cache.getUnchecked("null");
        assertThat(result, nullValue());
    }

    @Test
    public void testLoadNullValueUseOptional()
    {
        CacheLoader<String, Optional<Employee>> loader = new CacheLoader<String, Optional<Employee>>()
        {
            @Override
            public Optional<Employee> load(String key) throws Exception
            {
                if (key.equals("null"))
                    return Optional.fromNullable(null);
                else
                    return Optional.fromNullable(new Employee(key, key, key));
            }
        };

        LoadingCache<String, Optional<Employee>> cache = CacheBuilder.<String, Employee>newBuilder().build(loader);
        Employee alex = cache.getUnchecked("alex").get();
        assertThat(alex, notNullValue());


        assertThat(cache.getUnchecked("null").orNull(), nullValue());
    }

    @Test
    public void testCacheRefresh() throws InterruptedException
    {
        CacheLoader<String, Long> loader = CacheLoader.from(k ->
        {
            System.out.println("===");
            return System.currentTimeMillis();
        });
        LoadingCache<String, Long> cache = CacheBuilder.newBuilder()
//                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(loader);
        Long result1 = cache.getUnchecked("Alex");
        TimeUnit.SECONDS.sleep(2);
        Long result2 = cache.getUnchecked("Alex");
        System.out.println(result1);
        System.out.println(result2);
    }

    @Test
    public void testCachePreLoad()
    {
        CacheLoader<String, String> loader;
        loader = new CacheLoader<String, String>()
        {
            @Override
            public String load(String key)
            {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache;
        cache = CacheBuilder.newBuilder().build(loader);

        Map<String, String> map = new HashMap<>();
        map.put("first", "first");
        map.put("second", "SECOND");
        cache.putAll(map);

        assertThat(cache.size(), equalTo(2L));
        assertThat(cache.getUnchecked("first"), equalTo("first"));
    }


    @Test
    public void whenEntryRemovedFromCache_thenNotify() {
        CacheLoader<String, String> loader;
        loader = new CacheLoader<String, String>() {
            @Override
            public String load(final String key) {
                return key.toUpperCase();
            }
        };

        RemovalListener<String, String> listener;
        listener = new RemovalListener<String, String>() {
            @Override
            public void onRemoval(RemovalNotification<String, String> n){
                if (n.wasEvicted()) {
                    String cause = n.getCause().name();
                    assertEquals(RemovalCause.SIZE.toString(),cause);
                }
            }
        };

        LoadingCache<String, String> cache;
        cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .removalListener(listener)
                .build(loader);

        cache.getUnchecked("first");
        cache.getUnchecked("second");
        cache.getUnchecked("third");
        cache.getUnchecked("last");
        assertEquals(3, cache.size());
    }

}
