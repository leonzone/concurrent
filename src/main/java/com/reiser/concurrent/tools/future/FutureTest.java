package com.reiser.concurrent.tools.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author: reiserx
 * Date:2019/7/5
 * Des:
 */
public class FutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor
                = Executors.newFixedThreadPool(1);
        Result r = new Result();
        //可以通过操作 r ,实现线程间通讯
        Future<Result> future = executor.submit(new Task(r), r);
        Result fr = future.get();
    }

    static class Task implements Runnable {
        Result r;

        // 通过构造函数传入 result
        Task(Result r) {
            this.r = r;
        }

        @Override
        public void run() {
        }
    }

    static class Result {
    }
}
