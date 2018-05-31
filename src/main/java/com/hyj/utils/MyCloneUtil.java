package com.hyj.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MyCloneUtil {
	private MyCloneUtil() {  
        throw new AssertionError();  
    }  
  
    public static <T> T clone(T obj) throws Exception {  
        ByteArrayOutputStream bout = new ByteArrayOutputStream();  
        ObjectOutputStream oos = new ObjectOutputStream(bout);  
        oos.writeObject(obj);  
  
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());  
        ObjectInputStream ois = new ObjectInputStream(bin);  
        return (T) ois.readObject();  
          
        // 说明：调用ByteArrayInputStream或ByteArrayOutputStream对象的close方法没有任何意义  
        // 这两个基于内存的流只要垃圾回收器清理对象就能够释放资源  
    }  
}
