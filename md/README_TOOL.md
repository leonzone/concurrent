# JAVA 并发工具类


## 1、Executor 与线程池
### 线程池
- 线程是一个重量级对象，应该避免频繁创建和销毁。
- 线程池是一种生产者-消费者模式。自定义线程池

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
    - **corePoolSize**：最小或核心线程数
    - **maximumPoolSize**：线程池创建的的最大线程数
    - **keepAliveTime & unit**：定义线程多长时间没有执行任务，就记做空闲。
    - **workQueue**：工作队列
    - **threadFactory**：通过这个参数你可以自定义如何创建线程，例如你可以给线程指定一个有意义的名字。
    - **handler**: 拒绝策略。若线程池内所有线程都是忙碌，并且工作队列（有界队列）也满，线程池就会触发拒绝策略，以下为ThreadPoolExecutor提供的四种策略
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
- 不建议使用 Executor 建立线程池，因为很多都是无界队列 LinkedBlockingQueue，容易导致 OOM。
- 慎用默认拒绝策略。拒绝策略会 throw RejectedExecutionException 不强制处理容易忽略，建议自定义拒绝策略配合策略降级使用
- 异常处理不会通知所有需要按需捕获处理异常

## 2、Future
future 用以活得线程的执行结果。主要依赖于TreadPoolExecutor 3 个 submit() 方法 和 FutureTask 工具类

### submit()
TreadPoolExecutor 有 3 个 submit() 方法。[示例代码]()

- **Future<?> submit(Runnable task)**：提交 Runnable 任务，无返回结果。
- **<T> Future<T> submit(Callable<T> task)**:提交 Callable 任务，通过 get() 获得执行结果。
- **<T> Future<T> submit(Runnable task, T result)**:提交 Runnable 任务及结果引用。get()可以获得 result，result 相当于线程间的桥梁，可以作为共享数据,[示例代码]()

返回参数皆为 Future，可以进行以下操作：

- **boolean cancel(boolean mayInterruptIfRunning)**: 取消任务
- **boolean isCancelled()**:判断任务是否已取消
- **boolean isDone()**:判断任务是否已结束
- **get()**:获得任务执行结果,阻塞式的
- **get(long timeout, TimeUnit unit)**:获得任务执行结果，支持超时,阻塞式的

### FutureTask

FutureTask 是 Future 的一个工具类,继承自Runnable, Future。所以可以被 Thread 和 TreadPoolExecutor 执行，也能获取结果。[示例代码]()

### [烧水泡茶]()
![](http://media.xindapei.cn/2019-07-05-15622989352678.jpg)

- 等待动作实现的方式有： Thread.join()、CountDownLatch，阻塞队列，Future。
- 对于线程的分工问题，用一个有向图描述一下任务之间的关系。

## 3、CompletableFuture

