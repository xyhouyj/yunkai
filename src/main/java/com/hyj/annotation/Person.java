package com.hyj.annotation;

/**
 * Created by houyunjuan on 2018/6/26.
 */
@Description(value = "i am class interface")
public class Person {
    @Description("i am method interface")
    public String name() {
        return null;
    }
    public int age() {
        return 0;
    }
    @Deprecated
    public void sing() {
    }
}
