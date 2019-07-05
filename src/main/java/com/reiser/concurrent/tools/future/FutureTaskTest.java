package com.reiser.concurrent.tools.future;

import java.util.concurrent.*;

/**
 * @author: reiserx
 * Date:2019/7/5
 * Des:FutureTask 是 Future 的一个工具类,继承自Runnable, Future
 */
public class FutureTaskTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        useThreadPool();
        useThread();

    }

    private static void useThreadPool() throws InterruptedException, ExecutionException {
        // 创建 FutureTask
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1 + 2;
            }
        });
        // 创建线程池
        ExecutorService es = Executors.newCachedThreadPool();
        // 提交 FutureTask
        es.submit(futureTask);
        // 获取计算结果
        Integer result = futureTask.get();
        System.out.println(result);
    }

    private static void useThread() throws InterruptedException, ExecutionException {
        FutureTask<Integer> futureTask2 = new FutureTask<>(() -> 3 + 2);

        Thread thread = new Thread(futureTask2);
        thread.start();
        // 获取计算结果
        Integer result2 = futureTask2.get();
        System.out.println(result2);
    }
}
