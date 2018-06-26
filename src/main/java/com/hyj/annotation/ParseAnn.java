package com.hyj.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by houyunjuan on 2018/6/26.
 */
public class ParseAnn {
    public static void main(String[] args) throws ClassNotFoundException {
       Class c =  Class.forName("com.hyj.annotation.OneImpl");
       boolean isExist = c.isAnnotationPresent(Description.class);
       if (isExist){
           Description annotation = (Description) c.getAnnotation(Description.class);
           System.out.println(annotation.value() + "  " +annotation.age());
       }
       //找到方法上的注解
        Method[] methods = c.getMethods();
       for (Method method:methods){
           if(method.isAnnotationPresent(Description.class)) {
               Description annotation = (Description) method.getAnnotation(Description.class);
               System.out.println(annotation.value());
           }
       }

        for (Method method : methods) {
            Annotation[] as=method.getAnnotations();
            for (Annotation annotation : as) {
                if(annotation instanceof Description) {
                    Description a=(Description)annotation;
                    System.out.println(a.value());
                }
            }
        }
    }
}
