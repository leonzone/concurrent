package com.reiser.concurrent.features.error;

/**
 * @author: reiserx
 * Date:2019/6/25
 * Des:缓存导致的可见性问题
 */
public class Visibility {
    private  long count = 0;

    private void add10K() {
        int idx = 0;
        while (idx++ < 10000) {
            count += 1;
        }
        System.out.println("----get count----:"+count);
    }

    public long calc() throws InterruptedException {
        final Visibility test = new Visibility();
        // 创建两个线程，执行 add() 操作
        Thread th1 = new Thread(() -> {
            test.add10K();
        });
        Thread th2 = new Thread(() -> {
            test.add10K();
        });
        // 启动两个线程
        th1.start();
        th2.start();
        // 等待两个线程执行结束
        th1.join();
        th2.join();
        System.out.println("----get count----:"+count);
        return count;
    }

    public static void main(String[] args) {
        Visibility test = new Visibility();
        try {
            System.out.println(test.calc());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

