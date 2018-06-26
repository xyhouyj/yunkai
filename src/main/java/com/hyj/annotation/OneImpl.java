package com.hyj.annotation;

/**
 * Created by houyunjuan on 2018/6/26.
 */
@Description("i am a implements")
public class OneImpl implements Two{

    @Override
    public void speak() {
        System.out.println("two");
    }

    @Override
    public void print() {
        System.out.println("one");
    }
}
