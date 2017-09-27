package com.operate.common;

import java.util.Random;

/**
 * 冒泡排序
 * @author Administrator
 *
 */
public class BubbleSort {
	
public static Integer[] aa = null;
	
	public static void main(String[] args){
		long start = System.currentTimeMillis();
		insertData();
		bubbleSort();
		for(int a : aa){
			System.out.println(a);
		}
		long end = System.currentTimeMillis();
		long task = end - start;
		System.out.println("总耗时>>"+task);
	}
	
	
	public static void insertData(){
		int max = 1000;
		aa = new Integer[max];
		for(int i=1;i<=max;i++){
			Random random = new Random();// 定义随机类
			int result = random.nextInt(100);// 返回[0,100)集合中的整数，注意不包括100
			int n = i-1;
			aa[n] = result + i;
		}
	}
	
	public static void bubbleSort(){
		int nElems = aa.length;
		int in,out;
		for(out=nElems-1;out>1;out--){
			for(in=0;in<out;in++){
				if(aa[in] > aa[in+1]){
					swap(in, in+1);
				}
			}
		}
	}
	
	private static void swap(int one,int two){
		int temp = aa[one];
		aa[one] = aa[two];
		aa[two] = temp;
	}

}
