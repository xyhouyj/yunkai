package com.hyj.algorithm.bat;
import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.Math.max;

/**
 * @author houyunjuan
 */
public class StringTest {
	public static ArrayList<String> result = new ArrayList<String>();
	public static HashSet<String> set = new HashSet<String>();
	public static void main(String[] args) {

		/*String s = "abcdef";
		reverse(s,s.length(),2);*/
		//LCS问题  最长递增子序列
		/*String a = "ABCBDAB";
		String b = "BDCABA";
		String lcsStr = getLcs(a,b);
		System.out.println("LCS result is :" + lcsStr);*/
		//字符串全排列
		/*ArrayList<String> result = permutation("abc");
		for (String str:result
				) {
			System.out.println(str);
		}
		String s = "abcdef";
		String reverse = reverse(s);*/
//		System.out.println(reverse);
//		reverse(s,s.length(),2);



		//马拉车算法 最长回文字符串
		String str = "1221232121";
		// 按照马拉车算法 改造字符串
		//目的 将字符串的长度改为奇数  方便统一处理
		/*String res = "#";
		for (int i = 0; i < str.length(); i++) {
			res += str.charAt(i)+"#";
		}*/
		//回文半径数组
	/*	int[] arr = new int[res.length()];
		System.out.println(arr.length);
		System.out.println(res.toCharArray().length);
		String longestStr = manacher(res.toCharArray(),arr);
		System.out.println(longestStr);*/

		//KMP算法
		//从文本串text中 找出pattern第一次出现的位置
	/*	String text = "abc123edf";
		String pattern = "123";
		int firstIn = bruteForceSearch(text,pattern);
		System.out.println("BF result is" + firstIn);*/

		//求next数据
        String nextStr = "ABCDABD";
        int[] nextArr = getNextArr(nextStr);
		for (int i: nextArr
			 ) {
			System.out.println(i);
		}
		String text = "BBCABCDABABCDABCDABDE";
		int kmpResult = kmpByNext(text.toCharArray(),nextArr,nextStr);
		System.out.println("kmp result is " + kmpResult);
	}

	/**
	 * kmp算法查找模式串第一次出现的位置
	 * @param chars
	 * @param nextArr
	 * @return
	 */
	private static int kmpByNext(char[] chars, int[] nextArr,String nextStr) {
		int i = 0;
		int j = 0;
		int textLen = chars.length;
		int pLen = nextStr.length();
		while(i<textLen && j<pLen){
			if (j == -1 || chars[i] == nextStr.charAt(j)){
				j++;
				i++;
			}else{
				j = nextArr[j];
			}
		}
		if(j == pLen){
			return i - j;
		}else {
			return -1;
		}

	}

	/**
     * 获取pattern字符串的next数组
     * @param nextStr
     * @return
     */
    private static int[] getNextArr(String nextStr) {
        char[] chars = nextStr.toCharArray();
        int[] nextArr = new int[chars.length];
        int j = 0;
        int k = -1;
        nextArr[0] = -1;
        //根据j 求出j+1位的next数组的值
        while(j<chars.length -1){
            if(k == -1 || chars[j] == chars[k]){
                ++j;
                ++k;
                nextArr[j] = k;//算法中p  next[j] = next[j-1] + 1  假设条件就是next[j-1] = k
            }else{
                k =  nextArr[k];
            }
        }
        return nextArr;
    }

    /**
	 * 字符串全排列
	 * @param str
	 * @return
	 */
	public static ArrayList<String> permutation(String str){
		if (null == str || str.length() == 0){
			return result;
		}else{
			permutation(str,0,str.length() -1);
		}
		result.addAll(set);
		return result;
	}

	public static void permutation(String str, int start, int end){
		if(start == end){
			set.add(str);
		}else{
			char[] array = str.toCharArray();
			for (int i = start; i <= end ; i++) {
				if(i!=start){
					char tmp = array[start];
					array[start] = array[i];
					array[i] = tmp;
				}
				System.out.println("-------"+new String(array));
				permutation(String.valueOf(array), start + 1, array.length - 1);
//				System.out.println("*************"+new String(array));
				/*tmp = array[start];
				array[start] = array[i];
				array[i] = tmp;*/
//				System.out.println("@@@@@@@@@@@@"+new String(array));
			}

		}

	}
	/**
	 * 获取A b串的LCS
	 * @param a
	 * @param b
	 */
	public static String getLcs(String a,String b){
		//a b  变成字符数组
		char[] aArr = a.toCharArray();
		char[] bArr = b.toCharArray();
		int sizeA = a.length();
		int sizeB = b.length();
		int[][] chess = new int[sizeA +1][sizeB+1];//开辟一个二维数组
		for (int i = 0;i<= sizeA; i++){
			chess[i][0] = 0;//第0行
		}
		for(int j =0;j<= sizeB; j++){
			chess[0][j] = 0;//第0列
		}
		//填充定义的二维数组的内容
		for (int i = 1; i <= sizeA ; i++) {
			for (int j = 1; j <= sizeB ; j++) {
				//比较是否相等
				if (aArr[i-1] == bArr[j-1]){//一个一个比较  看是否相等
					//相等 取左上角的最大值 +1
					chess[i][j] = chess[i-1][j-1] + 1;
				}else{
					//不相等  取左边和上面的最大值
					chess[i][j] = Math.max(chess[i][j-1],chess[i-1][j]);
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		//根据二维数组的内容 获取lcs  从右下角开始  往左上角遍历
		int i = sizeA;
		int j = sizeB;
		while (i != 0 && j!=0){
			if (aArr[i-1] == bArr[j-1]){
				sb.append(aArr[i-1]);
				i--;
				j--;
			}else{
				if(chess[i][j-1] > chess[i-1][j]){
					j--;
				}else{
					i--;
				}
			}
		}
		System.out.println("the longest common strs is " + sb.toString());//ABCB
		String result = reversStringMethod(sb.toString());
		System.out.println("the result is " + result);//BCBA
		return result;
	}

	/**
	 * 字符串反转  递归获取
	 * @param str
	 * @return
	 */
	public static String reversStringMethod(String str){
		if(str.length() == 1){
			return str;
		}
		return  reversStringMethod(str.substring(1)) + str.charAt(0);
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
	private static String reverse(String s, int m, int n) {
		char[] charArray = s.toCharArray();
		n %= m;
		reverseString(charArray,0,n-1);
		reverseString(charArray,n,m-1);
		reverseString(charArray,0,m-1);
		System.out.println(new String(charArray));
		return new String(charArray);
	}
	private static void reverseString(char[] charArray, int from, int to) {
		while(from < to){
			//交换两个字符的位置
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
