package com.llm.algorithm.sort;


import java.util.Arrays;

public class QuickSort {

    public static void main(String[] args) {
        int[] arrs = new int[]{3, 4, 22,5, 67, 7, 1};
        quickSort(arrs, 0, arrs.length - 1);
        Arrays.stream(arrs).forEach(System.out::println);
    }

    public static void quickSort(int[] arr, int left, int right){
        if (arr == null || arr.length == 0){
            return;
        }
        if (left >= right){
            return;
        }
        int i = left;
        int j = right;

        int std = arr[left];

        while (i != j){
            while (i < j && arr[j] >= std){
                j--;
            }
            while (i < j && arr[i] <= std){
                i++;
            }
            if (i < j){
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        }

        arr[left] = arr[i];
        arr[i] = std;

        quickSort(arr, left, j - 1);
        quickSort(arr, j + 1, right );

    }

}
