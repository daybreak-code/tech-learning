package com.llm.threads.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinThreadPool {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinThreadPool = new ForkJoinPool();
        MyRecursiveTask myRecursiveTask = new MyRecursiveTask(24);
        ForkJoinTask<Long> submit = forkJoinThreadPool.submit(myRecursiveTask);
        System.out.println(submit.get());
    }

    static class MyRecursiveTask extends RecursiveTask<Long> {

        private long workload = 0;

        public MyRecursiveTask(long workload) {
            this.workload = workload;
        }

        public List<MyRecursiveTask> createSubTasks(){
            ArrayList<MyRecursiveTask> subTasks = new ArrayList<>();
            MyRecursiveTask subTask01 = new MyRecursiveTask(workload / 2);
            MyRecursiveTask subTask02 = new MyRecursiveTask(workload / 2);
            subTasks.add(subTask01);
            subTasks.add(subTask02);
            return subTasks;
        }

        @Override
        protected Long compute() {
            if (this.workload > 16){
                System.out.println("Spliting workload: "+ workload);
                ArrayList<MyRecursiveTask> subTasks = new ArrayList<>();
                subTasks.addAll(createSubTasks());
                for (MyRecursiveTask subTask : subTasks) {
                    subTask.fork();
                }
                long result = 0;
                for (MyRecursiveTask subTask : subTasks) {
                    result += subTask.join();
                }
                return result;
            }else {
                System.out.println("Start to work, workload is: " + workload);
                return 3 * workload;
            }
        }
    }
}
