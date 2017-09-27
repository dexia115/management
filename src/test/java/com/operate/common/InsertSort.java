package com.operate.common;

import java.util.Random;

/**
 * 插入排序
 * @author Administrator
 *
 */
public class InsertSort {
	
	public static Integer[] aa = null;
	
	public static void main(String[] args){
		long start = System.currentTimeMillis();
		insertData();
		insertSort();
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
	
	
	public static void insertSort(){
		int nElems = aa.length;
		int in,out;
		for(out=1;out<nElems;out++){
			int temp = aa[out];
			in = out;
			while(in>0 && aa[in-1] >= temp){
				aa[in] = aa[in-1];
				--in;
			}
			aa[in] = temp;
		}
	}

}
