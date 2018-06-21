package com.hyj.algorithm.bat;

/**
 * 数组测试类
 * 类型相同的若干元素的有序集合
 * 数组的下标和元素建立单向联系
 * 通过下标查找O(1);无序查找为O(N);有序的O(logN) [二分法查找]
 * Created by houyunjuan on 2018/6/7.
 */
public class ArrayTest {
    public static void main(String[] args) {
        int[] arr = {8,8,1,1,1,8,1,1,6,1,8};
//        int result = getZhongShu(arr);
//        System.out.println("绝对众数为：" + result);
//        get13ZhongZhu(arr);
        int[] arr2 = {3,5,1,2,-3,7,4,8};
        int[] arr3 = {0,3,5,1,2,-3,7,4,8};
        int firstMiss = firstMissNumber(arr3,arr3.length-1);
        System.out.println("first miss number is " + firstMiss);
    }

    /**
     * 获取绝对众数
     * @param arr
     * @return
     */
    private static int getZhongShu(int[] arr) {
            int m = arr[0];//记录当前的绝对众数
            int count = 0;
            for(int i = 0;i<arr.length;i++){
                if(count == 0){
                    m = arr[i];
                    count = 1;
                }else if(m == arr[i]){
                    count++;
                }else if(m != arr[i]){
                    //count--  和在数组里面 同时删掉m 和 arr[i] 的效果一样
                    count--;
                }
            }
            return m;
    }

    /**
     * 获取三分之一绝对众数
     */
    public static void get13ZhongZhu(int[] arr){
        int cm =0,cz=0;
        int m = arr[0],z = arr[0];
        for(int i = 0;i<arr.length;i++){
            if(cm == 0){
                cm = 1;
                m = arr[i];
            }else if(cz == 0){
                cz = 1;
                z = arr[0];
            }else if(m == arr[i]){
                cm++;
            }else if(z == arr[i]){
                cz++;
            }else {
                cm--;
                cz--;
            }
        }

        cm = 0;
        cz = 0;
        for (int i:arr
             ) {
            if(m == i){
                cm ++;
            }
            if(z == i){
                cz++;
            }
        }
        System.out.println(m+"===" +cm);
        System.out.println((z + "====" +cz));
    }

    /**
     * 求局部最大值
     * @param arr
     * @return
     */
    public static int localMaximum(int[] arr){
        int left = 0;
        int right = arr.length -1;
        while(left <= right){
            int mid = (left + right)/2;
            if(arr[mid]>arr[mid+1]){
                right = mid;
            }
            if(arr[mid] < arr[mid+1]){
                left = mid +1;
            }
        }
        return arr[left];
    }

    /**
     * 获取第一个缺失的整数
     * @param arr
     * @param size
     * @return
     */
    public static int firstMissNumber(int[] arr, int size){
        int i = 1;
        while(i <= size){
            if(arr[i] == i){
                i++;
            }else if((arr[i]<i) || (arr[i]>size) || (arr[i] == arr[arr[i]])){
                //通过将最后一个 移动到i位置 来抛弃元素
                arr[i] = arr[size];
                size --;
            }else{
                int tmp = arr[i];
                int result = arr[arr[i]];
                arr[tmp] = arr[i];
                arr[i] = result;
               /* int tmp = arr[arr[i]];
                arr[i] = arr[arr[i]];
                arr[arr[i]] = tmp;*/
            }
        }
        return i;
    }
}
