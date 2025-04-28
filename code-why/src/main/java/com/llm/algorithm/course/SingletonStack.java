package com.llm.algorithm.course;

import java.util.Stack;

public class SingletonStack {
    public int[] dailyTemperatures(int[] T){
        int[] result = new int[T.length];
        Stack<Integer> stack = new Stack<>();

        for(int i = 0; i < T.length;i++){
            while (!stack.isEmpty() && T[i] > T[stack.peek()]){
                int idx = stack.pop();
                result[idx] = i - idx;
            }
            stack.push(i);
        }
        return result;
    }
}
