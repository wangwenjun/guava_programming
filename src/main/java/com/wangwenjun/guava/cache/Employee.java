package com.wangwenjun.guava.cache;

import com.google.common.base.MoreObjects;

/***************************************
 * @author:Alex Wang
 * @Date:2017/11/18
 * QQ: 532500648
 * QQ群:463962286
 ***************************************/
public class Employee
{
    private final String name;
    private final String dept;
    private final String empID;
    private final byte[] data = new byte[1024 * 1024];

    public Employee(String name, String dept, String empID)
    {
        this.name = name;
        this.dept = dept;
        this.empID = empID;
    }

    public String getName()
    {
        return name;
    }

    public String getDept()
    {
        return dept;
    }

    public String getEmpID()
    {
        return empID;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("Name", this.getName()).add("Department", getDept())
                .add("EmployeeID", this.getEmpID()).toString();
    }

    @Override
    protected void finalize() throws Throwable
    {
        System.out.println("The name " + getName() + " will be GC.");
    }
}