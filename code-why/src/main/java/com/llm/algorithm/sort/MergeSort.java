package com.llm.algorithm.sort;

public class MergeSort {

    public void mergeSort(int[] arr){
        if (arr == null || arr.length < 2){
            return;
        }
        int[] tmp = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1, tmp);
    }

    public void mergeSort(int[] arr, int left, int right, int[] tmp){
        if (left < right){
            int mid = (right + left) / 2;

            mergeSort(arr, left, mid, tmp);
            mergeSort(arr, mid + 1, right, tmp);

            mergeSort(arr, left, mid, right, tmp);
        }
    }

    public void mergeSort(int[] arr, int left, int mid, int right, int[] tmp){

        //1. copy 1/2
        System.arraycopy(arr, left, tmp, left, mid - left + 1);

        int i = left;
        int j = mid + 1;
        int t = left;

        while (i <= mid && j <= right){
            if (tmp[i] <= arr[j]){
                arr[t++] = tmp[i++];
            }else {
                arr[t++] = arr[j++];
            }
        }

        while (i <= mid){
            arr[t++] = tmp[i++];
        }



    }

}
