package com.hyj.algorithm.bat;

import java.util.Arrays;

public class StringTest {
	public static void main(String[] args) {
		String s = "abcdef";  
		String reverse = reverse(s);
//		System.out.println(reverse);
//		reverse(s,s.length(),2);
		
		
		
		//马拉车算法 最长回文字符串
		String str = "1221232121";
		// 按照马拉车算法 改造字符串
		//目的 将字符串的长度改为奇数  方便统一处理
		String res = "#";
		for (int i = 0; i < str.length(); i++) {
			res += str.charAt(i)+"#";
		}
		//回文半径数组
		int[] arr = new int[res.length()];
		System.out.println(arr.length);
		System.out.println(res.toCharArray().length);
		String longestStr = manacher(res.toCharArray(),arr);
		System.out.println(longestStr);
		
		//KMP算法
		//从文本串text中 找出pattern第一次出现的位置
		String text = "abc123edf";
		String pattern = "123";
		int firstIn = bruteForceSearch(text,pattern);
		System.out.println("BF result is" + firstIn);
		
	}
	/**
	 * 暴力求解
	 * @param text
	 * @param pattern
	 * @return
	 */
	private static int bruteForceSearch(String text, String pattern) {
		int size = text.length();
		int pSize = pattern.length();
		int i=0,j=0;
		if (pSize>size) {
			return -1;
		}
		while((i<=size-pSize) && j<pSize){
			if (text.charAt(i+j) == pattern.charAt(j)) {
				j++;
			}else {
				i++;
				j=0;
			}
		}
		if (j >= pSize) {
			return i;
		}
		return -1;
	}
	/**
	 * 根据马拉车算法 求最长回文字符串 先求出P
	 * @param str
	 * @return
	 */
	private static String manacher(char[] str,int[] arr) {
		//初始化
		int id = 0;
		int mx = 1;
		arr[0] = 1;
		for (int i = 1; i < str.length; i++) {
			if (mx>i) {
				arr[i] = Math.min(arr[2*id -i], mx-i);
			}else {
				arr[i] = 1;
			}
			if ((i+arr[i]) <= (str.length -1) ) {//避免由于i+arr[i]导致数据越界
				for(;str[i+arr[i]] == str[i-arr[i]];){
					arr[i] = arr[i] + 1;
					if (((i-arr[i]) <= 0) || ((i+arr[i])>(str.length-1))) {
						break;
					}
				}
				
			}
			if(mx < i+arr[i]){
				id = i;
				mx = i+ arr[i];
			}
		}
		int max = 0;
		for (int i = 0; i < arr.length; i++) {
			if (max<arr[i]) {
				max = arr[i];
			}
		}
		return Integer.toString(max-1);
	}
	
	/**
	 * �ַ���ѭ����λ  ʱ�临�Ӷ�O(N)  �ռ临�Ӷ�O(1)  ��������ת��
	 * @param s
	 * @param m  ����
	 * @param n  ǰnλ
	 */
	private static void reverse(String s, int m, int n) {
		char[] charArray = s.toCharArray();
		n %= m;
		reverseString(charArray,0,n-1);
		reverseString(charArray,n,m-1);
		reverseString(charArray,0,m-1);
		System.out.println(new String(charArray));
	}
	private static void reverseString(char[] charArray, int from, int to) {
		while(from < to){
			//�Ƴ�  ��ֵ  ����
			char c = charArray[to];
			charArray[to--] = charArray[from];
			charArray[from++] = c;
		}
		
	}
	
	/**
	 * 字符串反转
	 * @param originStr
	 * @return
	 */
	public static String reverse(String originStr) {  
        if(originStr == null || originStr.length() <= 1)   
            return originStr;  
        return reverse(originStr.substring(1)) + originStr.charAt(0);  
    }  
}
