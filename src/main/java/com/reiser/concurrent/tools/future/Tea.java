package com.reiser.concurrent.tools.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author: reiserx
 * Date:2019/7/5
 * Des:烧水泡茶问题
 */
public class Tea {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> task2 = new FutureTask<>(new T2Task());
        FutureTask<String> task1 = new FutureTask<>(new T1Task(task2));


        new Thread(task1).start();
        new Thread(task2).start();

        System.out.println(task1.get());
    }


    static class T1Task implements Callable<String> {
        FutureTask<String> t2;

        public T1Task(FutureTask<String> t2) {
            this.t2 = t2;
        }

        @Override
        public String call() throws Exception {
            System.out.println("洗水壶...");
            Thread.sleep(1000);

            System.out.println("烧开水...");
            Thread.sleep(5000);
            System.out.println("水开了...咕噜咕噜");
            //get 是阻塞式的
            String tf = t2.get();
            System.out.println("拿到茶叶..." + tf);

            System.out.println("泡茶..." + tf);

            return "上茶" + tf;
        }
    }


    static class T2Task implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("洗茶叶...");
            Thread.sleep(1000);

            System.out.println("洗茶杯...");
            Thread.sleep(2000);

            System.out.println("拿茶叶...");
            Thread.sleep(10000);

            return "龙井";
        }
    }
}
