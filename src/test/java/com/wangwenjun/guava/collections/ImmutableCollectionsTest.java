package com.wangwenjun.guava.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/***************************************
 * @author:Alex Wang
 * @Date:2018/1/15
 * QQ: 532500648
 * QQ群:463962286
 ***************************************/
public class ImmutableCollectionsTest
{

    @Test
    public void test()
    {
        ImmutableList<Integer> list = ImmutableList.of(1, 2, 3, 4, 5, 6);
        System.out.println(list);
        list.add(7);

        System.out.println(list);
    }

    @Test
    public void testCopy()
    {
        Integer[] array = {1, 2, 3, 4, 5};
        List<Integer> list = ImmutableList.copyOf(array);
        System.out.println(list);
    }

    @Test
    public void testBuild()
    {
        ImmutableList<Object> result = ImmutableList.builder().add(1)
                .add(2).add(4, 5, 6).build();
        System.out.println(result);
    }

    @Test
    public void testMap()
    {
        ImmutableMap<String, String> map = ImmutableMap.of("Java", "1.8");
        System.out.println(map);
    }

    @Test
    public void testOrder()
    {
        /*List<Integer> toSort = Arrays.asList(1, 2, 3, 4, null, 6,0);
        Collections.sort(toSort);
        System.out.println(toSort);*/
        List<Integer> toSort = Arrays.asList(1, 2, 3, 4, null, 6,0);
        Collections.sort(toSort,Ordering.natural().nullsFirst());
        System.out.println(toSort);

        Collections.sort(toSort,Ordering.natural().nullsLast());
        System.out.println(toSort);

        List<Integer> list = Arrays.asList(3, 5, 4, 1, 2);
        Collections.sort(list,Ordering.natural());
        System.out.println(list);

        assertThat(Ordering.natural().isOrdered(list),is(true));


        Collections.sort(list,Ordering.natural().reverse());
        System.out.println(list);
    }
}