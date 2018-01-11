package com.wangwenjun.guava.cache;

import com.google.common.cache.*;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/***************************************
 * @author:Alex Wang
 * @Date:2018/1/11
 * QQ: 532500648
 * QQ群:463962286
 ***************************************/
public class CacheLoaderTest4
{

    @Test
    public void testCacheStat()
    {
        CacheLoader<String, String> loader = CacheLoader.from(String::toUpperCase);
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().recordStats().build(loader);

        CacheStats stats = cache.stats();
        cache.getUnchecked("Alex");
        assertThat(stats.missCount(),equalTo(1));

        String configString = "concurrencyLevel=10,refreshAfterWrite=5s";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(configString);
        CacheBuilder.from(spec);
    }
}
