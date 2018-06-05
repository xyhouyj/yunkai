package com.hyj.distribute.lock;

public class TestOne {
	 public static void main(String[] args) {  
	        String a = "Programming";  
	        String b = new String("Programming");  
	        String c = "Program" + "ming";  
	          
	        System.out.println(a == b);  
	        System.out.println(a == c);  
	        System.out.println(a.equals(b));  
	        System.out.println(a.equals(c));  
	        System.out.println(a.intern() == b.intern());  
	        
	        
	    }  
	 public String getStr(){
		 return null;
	 }
	 public String getStr(String a){
		 return null;
	 }
	 public String getStr(Integer a){
		 return null;
	 }
}
