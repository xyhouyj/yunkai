package com.hyj.annotation;

import java.io.IOException;

/**
 * Created by houyunjuan on 2018/6/26.
 */
@TestAnnotation(count = 0x7fffffff)
public class TestMain {
    public static void main(String[] args) throws IOException {
        TestAnnotation annotation = TestMain.class.getAnnotation(TestAnnotation.class);
        System.out.println(annotation.count());
        System.in.read();
    }
}
