package com.uetty.common.doc.tester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Sort {

	/**
	 * 选择排序
	 * <p>每次与最大值相比，找出最大值的位置
	 */
	public static List<Integer> selectionSort(List<Integer> source) {
		List<Integer> list = new ArrayList<>(source);
		long st = System.currentTimeMillis();
		for (int lastIndex = list.size() - 1; lastIndex > 0; lastIndex--) {// 最大值最终安放的位置
			int maxIndex = 0;
			for (int j = 1; j <= lastIndex; j++) {// 将最大值的位置查找出来
				if (list.get(maxIndex) < list.get(j)) {
					maxIndex = j;
				}
			}
			
			if (maxIndex != lastIndex) {// 交换最大值的位置与最终安放位置的值交换
				// 交换值的次数少，这是选择排序的优势
				Integer max = list.get(maxIndex);
				list.set(maxIndex, list.get(lastIndex));
				list.set(lastIndex, max);
			}
			// 劣势，比较次数多
		}
		System.out.println("\nselection sort");
		System.out.println(System.currentTimeMillis() - st);
//		System.out.println(list);
		return list;
	}
	
	/**
	 * 冒泡排序
	 * <p>每次比相邻元素，将更大元素一级一级推到最后面（就跟冒泡一样）
	 */
	public static List<Integer> bubbleSort(List<Integer> source) {
		List<Integer> list = new ArrayList<>(source);
		long st = System.currentTimeMillis();
		for (int lastIndex = list.size() - 1; lastIndex > 0; lastIndex--) {// 最大值冒泡到的位置
			boolean exchange = false;
			for(int j = 0; j < lastIndex; j++) {// 将最大的数一级一级的冒泡到最后
				Integer cacheJ = list.get(j);
				if (cacheJ > list.get(j + 1)) {
					// 交换
					list.set(j, list.get(j + 1));
					list.set(j + 1, cacheJ);
					exchange = true;
				}
			}
			if (!exchange) {// 该轮未交换过，说明已经全部有序
				// 可以检测提前完成排序，这正是是冒泡排序的优势点
				break;
			}
			// 劣势，交换次数可能会比较多
		}
		System.out.println("\nbubble sort");
		System.out.println(System.currentTimeMillis() - st);
//		System.out.println(list);
		return list;
	}

	/**
	 * 快速排序中值归位
	 * <p>把更大的数放右边，更小的数放左边（左右交替收缩范围进行，保证上次置换的出值位置作为本次置换的入值位置）
	 */
	private static void quickSortMedianPlace(List<Integer> list, int left, int right) {
		if (left >= right) return;
		int i = left;// 左边游标值
		int j = right;// 右边游标值
		Integer medianValue = list.get(i);// 初始以左边界值作为中间值
		int posDir = -1;// 初始从右往左检测
		while (i != j) {// 当i与j位置重叠
			if (posDir > 0) {// 从左往右
				if (list.get(i) > medianValue) {// 在左边找到比中间值大的值
					list.set(j, list.get(i));// 将找到的值放到右边上次出值的位置
					posDir *= -1;// 查找方向交替
					j--;// 右边的游标值减少
				} else {
					i++;
				}
			} else {// 从右往左
				if (list.get(j) < medianValue) {// 在右边找到比中间值小的值
					list.set(i, list.get(j));// 找到的值放到左边的上次出值的位置
					posDir *= -1;// 查找方向交替
					i++;// 左边游标值增加
				} else {
					j--;
				}
			}
		}
		// 找到了中间值的位置
		list.set(i, medianValue);
		// 左右区间分别递归
		// 快速排序存在扩展性，可以使用多线程排序加速
		quickSortMedianPlace(list, left, i - 1);
		quickSortMedianPlace(list, i + 1, right);
	}
	
	public static List<Integer> quickSort1(List<Integer> source) throws InterruptedException {
		List<Integer> list = new ArrayList<>(source);
		long st = System.currentTimeMillis();
		
//		quickSortMedianPlace(list, 0, list.size() - 1);
		CountDownLatch cdl = new CountDownLatch(1);
		new Mt(list, 0, list.size() - 1, cdl).start();;
		cdl.await();
		System.out.println("\nquick sort");
		System.out.println(System.currentTimeMillis() - st);
//		System.out.println(list);
		return list;
	}
	
	public static List<Integer> quickSort(List<Integer> source) throws InterruptedException {
		List<Integer> list = Collections.synchronizedList(new ArrayList<>(source));
		long st = System.currentTimeMillis();
		
		quickSortMedianPlace(list, 0, list.size() - 1);
		System.out.println("\nquick sort");
		System.out.println(System.currentTimeMillis() - st);
//		System.out.println(list);
		return list;
	}

	
	static class Mt extends Thread {
		List<Integer> list;
		int left;
		int right;
		CountDownLatch cdl;
		public Mt(List<Integer> list, int left, int right, CountDownLatch cdl) {
			this.list = list;
			this.left = left;
			this.right = right;
			this.cdl = cdl;
		}
		@Override
		public void run() {
			quickSortMedianPlace(list, left, right);
			if (cdl != null) {
				cdl.countDown();
			}
		}
		
		private void quickSortMedianPlace(List<Integer> list, int left, int right) {
			if (left >= right) return;
			int i = left;// 左边游标值
			int j = right;// 右边游标值
			Integer medianValue = list.get(i);// 初始以左边界值作为中间值
			int posDir = -1;// 初始从右往左检测
			while (i != j) {// 当i与j位置重叠
				if (posDir > 0) {// 从左往右
					if (list.get(i) > medianValue) {// 在左边找到比中间值大的值
						list.set(j, list.get(i));// 将找到的值放到右边上次出值的位置
						posDir *= -1;// 查找方向交替
						j--;// 右边的游标值减少
					} else {
						i++;
					}
				} else {// 从右往左
					if (list.get(j) < medianValue) {// 在右边找到比中间值小的值
						list.set(i, list.get(j));// 找到的值放到左边的上次出值的位置
						posDir *= -1;// 查找方向交替
						i++;// 左边游标值增加
					} else {
						j--;
					}
				}
			}
			// 找到了中间值的位置
			list.set(i, medianValue);
			
			// 左右区间分别递归
			// 快速排序存在扩展性，可以使用多线程排序加速
			CountDownLatch childCdl = null;
			int count = 0; 
			if (i - 1 - left > 500000 && right - i - 1 > 500000) {
				count = 1;
				childCdl = new CountDownLatch(count);
			}
			if (i - 1 - left > 500000) {
				new Mt(list, left, i - 1, childCdl).start();;
			} else {
				quickSortMedianPlace(list, left, i - 1);
			}
			quickSortMedianPlace(list, i + 1, right);
			
			if (count > 0) {
				try {
					childCdl.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 3000000; i++) {
			list.add((int)(Math.random() * 10000000));
		}
		System.out.println("sort start");
//		System.out.println(list);
		
//		selectionSort(list);
		
//		bubbleSort(list);
		
		quickSort1(list);
		
//		quickSort(list);
		
	}
}
