package com.reiser.concurrent.tools.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: reiserx
 * Date:2019/7/4
 * Des:自定义线程池，简化的线程池，仅用来说明工作原理
 */
class MyThreadPool {
    /**
     * 利用阻塞队列实现生产者 - 消费者模式
     */
    BlockingQueue<Runnable> workQueue;
    /**
     * 保存内部工作线程
     */
    List<WorkerThread> threads
            = new ArrayList<>();

    MyThreadPool(int poolSize,
                 BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        // 创建工作线程
        for (int idx = 0; idx < poolSize; idx++) {
            WorkerThread work = new WorkerThread();
            work.start();
            threads.add(work);
        }
    }

    /**
     * 提交任务
     */
    void execute(Runnable command) {
        try {
            workQueue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 工作线程负责消费任务，并执行任务
     */

    class WorkerThread extends Thread {
        @Override
        public void run() {
            // 循环取任务并执行
            while (true) {
                Runnable task = null;
                try {
                    task = workQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.run();
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue workQueue = new LinkedBlockingQueue(10);
        MyThreadPool threadPool = new MyThreadPool(10, workQueue);
        threadPool.execute(() -> System.out.println("hello thread"));
    }
}

