package com.hyj.algorithm.bat;

public class SuanFa {
	public static void main(String[] args) {
		int[] arr = {1,2,3,4,6,8,9};
		int result = search(0,arr.length-1,4,arr);
		System.out.println(result);
		System.out.println(searchTwo(0,arr.length-1,4,arr));
	}

	private static int search(int start, int end, int source,int[] arr) {
		// TODO Auto-generated method stub
		int mid = (start + end)/2;
		if (start > end) {
			return -1;
		}else if(arr[mid]==source){
			return mid;
		}else if(arr[mid]>source) {
			end = mid;
		}else if(arr[mid]<source){
			start = mid+1;
		}
		return search(start,end,source,arr);
	}
	
	private static int searchTwo(int start, int end, int source,int[] arr) {
		// TODO Auto-generated method stub
		int result = -1;
		int mid = (start + end)/2;
		while(start < end){
			if(arr[mid]==source){
				result = mid;
			}else if(arr[mid]>source) {
				end = mid;
			}else if(arr[mid]<source){
				start = mid+1;
			}
			mid = (start + end)/2;
		}
		return result;
	}
}
