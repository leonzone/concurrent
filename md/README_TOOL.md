# JAVA 并发工具类


## 1、Executor 与线程池
### 线程池
- 线程是一个重量级对象，应该避免频繁创建和销毁。
- 线程池是一种生产者-消费者模式。[自定义线程池](../src/main/java/com/reiser/concurrent/tools/threadpool/MyThreadPool.java)

### Java 使用线程池 ThreadPoolExecutor

``` java
ThreadPoolExecutor(
  int corePoolSize,
  int maximumPoolSize,
  long keepAliveTime,
  TimeUnit unit,
  BlockingQueue<Runnable> workQueue,
  ThreadFactory threadFactory,
  RejectedExecutionHandler handler) 
```
- 参数意义
    - **corePoolSize：**最小或核心线程数
    - **maximumPoolSize：**线程池创建的的最大线程数
    - **keepAliveTime & unit：**定义线程多长时间没有执行任务，就记做空闲。
    - **workQueue：**工作队列
    - **threadFactory：**通过这个参数你可以自定义如何创建线程，例如你可以给线程指定一个有意义的名字。
    - handler: 拒绝策略。若线程池内所有线程都是忙碌，并且工作队列（有界队列）也满，线程池就会触发拒绝策略，以下为ThreadPoolExecutor提供的四种策略
        - CallerRunsPolicy：提交任务的线程自己去执行该任务。
        - AbortPolicy：默认的拒绝策略，会 throws RejectedExecutionException
        - DiscardPolicy：直接丢弃任务，没有任何异常抛出。
        - DiscardOldestPolicy：丢弃最老的任务，其实就是把最早进入工作队列的任务丢弃，然后把新任务加入到工作队列。
- jdk 1.6之后加入allowCoreThreadTimeOut(boolean value) 核心线程也可释放。

### Executor
Executors提供了一些创建线程池的工具方法。
- Executors.newSingleThreadExecutor()：只能同时运行一个线程的线程池，如果有多个需要排队执行。
- Executors.newFixedThreadPool(int nThreads)：创建了一个固定大小的线程池，但是可以指定同时运行的线程数量为nThreads。
- Executors.newCachedThreadPool()：

### 注意事项
- 不建议使用 Executor 建立线程池，因为很多都是无界队列 LinkedBlockingQueue，容易导致 OOM。[可以自定义](../src/main/java/com/reiser/concurrent/tools/threadpool/ThreadPoolBuilder.java)
- 慎用默认拒绝策略。拒绝策略会 throw RejectedExecutionException 不强制处理容易忽略，建议[自定义拒绝策略配合策略降级使用](../src/main/java/com/reiser/concurrent/tools/threadpool/AbortPolicyWithReport.java)
- 异常处理不会通知所有需要按需捕获处理异常
