package com.hyj.algorithm.bat;

import java.util.Arrays;

public class StringTest {
	public static void main(String[] args) {
		String s = "abcdef";
		reverse(s,s.length(),2);
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
	
	
	
}
