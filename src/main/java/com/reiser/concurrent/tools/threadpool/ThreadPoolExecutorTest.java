package com.reiser.concurrent.tools.threadpool;

import java.util.concurrent.*;

/**
 * @author: reiserx
 * Date:2019/7/4
 * Des:
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) {

        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), ThreadPoolUtil.buildThreadFactory("test"), new AbortPolicyWithReport(""));

        singleThreadPool.execute(() -> System.out.println(Thread.currentThread().getName()));
        singleThreadPool.shutdown();

    }


}
