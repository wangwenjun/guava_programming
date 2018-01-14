package com.wangwenjun.guava.collections;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/***************************************
 * @author:Alex Wang
 * @Date:2018/1/14
 * QQ: 532500648
 * QQ群:463962286
 ***************************************/
public class BiMapExampleTest
{

    @Test
    public void testCreateAndPut()
    {
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("1", "2");
        biMap.put("1", "3");
        assertThat(biMap.containsKey("1"), is(true));
        assertThat(biMap.size(), equalTo(1));

        try
        {
            biMap.put("2", "3");
            fail();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testBiMapInverse()
    {
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("1", "2");
        biMap.put("2", "3");
        biMap.put("3", "4");

        assertThat(biMap.containsKey("1"), is(true));
        assertThat(biMap.containsKey("2"), is(true));
        assertThat(biMap.containsKey("3"), is(true));
        assertThat(biMap.size(), equalTo(3));

        BiMap<String, String> inverseKey = biMap.inverse();
        assertThat(inverseKey.containsKey("2"), is(true));
        assertThat(inverseKey.containsKey("3"), is(true));
        assertThat(inverseKey.containsKey("4"), is(true));
        assertThat(inverseKey.size(), equalTo(3));
    }

    @Test
    public void testCreateAndForcePut()
    {
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("1", "2");
        assertThat(biMap.containsKey("1"), is(true));
        biMap.forcePut("2", "2");
        assertThat(biMap.containsKey("1"), is(false));
        assertThat(biMap.containsKey("2"), is(true));
    }
}