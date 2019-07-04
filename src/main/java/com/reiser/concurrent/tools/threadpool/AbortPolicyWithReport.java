package com.reiser.concurrent.tools.threadpool;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: reiserx
 * Date:2019/7/4
 * Des: 自定义拒绝策略
 */
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

    private final String threadName;


    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String msg = String.format(
                "Thread pool is EXHAUSTED!"
                        + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
                        + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)!",
                threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(),
                e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(),
                e.isTerminating());
        throw new RejectedExecutionException(msg);
    }

}
