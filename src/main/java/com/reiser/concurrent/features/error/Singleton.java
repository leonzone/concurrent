package com.reiser.concurrent.features.error;

/**
 * @author: reiserx
 * Date:2019/6/30
 * Des:编译优化带来的有序性问题
 */
public class Singleton {
    private static Singleton instance;
    private Singleton(){}
    public static Singleton getInstance(){
        //一重判断
        if (instance == null) {
            synchronized(Singleton.class) {
                //二重判断防止多线程同时竞争锁的情况多次创建
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
