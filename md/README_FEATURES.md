## 1、开始
### 如何学好并发编程
- 跳出来，看全景
- 钻转进去，看本质。工程上的问题，一定要有理论基础
## 2、并发问题发生的本质
### 三个核心问题
- **分工**，如何高效地拆解任务并分配给线程
- **同步**，一个线程完成任务，如何通知后续线程开工的问题线程协作的本质：当某个条件不满足时，线程需要等待，当某个条件满足时，线程需要被唤醒执行​​
- **互斥**，同一时刻，只允许一个线程访问共享变量

![](http://media.xindapei.cn/2019-06-30-15618874987474.jpg)

### 并发 Bug 的源头
本质原因是 CPU、内存和 I/O 的读写速度不一致导致的。硬件上采用 「CPU 缓存」调解 CPU与内存的速度；采用「CPU 分时复用」协调 CPU 和 I/O 设备的速度差异；编译程序优化指令执行次序，使得缓存能够得到更加合理地利用。但也导致了软件问题：

- **缓存导致的可见性问题**
一个线程对共享变量的修改，另外一个线程能够立刻看到，我们称为「可见性」。
单核 CPU 对变量的修改，不同线程间获取的值一致，不存在可见性问题。但多核因为不同 CPU 的缓存不同，导致了可见性问题。
![](http://media.xindapei.cn/2019-06-30-15618883059294.jpg)

- **线程切换导致的原子性问题**
我们把一个或者多个操作在 CPU 执行的过程中不被中断的特性称为「原子性」。
一条高级语言包括多条 CPU 指令，如：count=+1。分时复用会导致破坏语句的原子性，需要我们在高级语言层面保证操作的原子性
![](http://media.xindapei.cn/2019-06-30-15618886559448.jpg)

- **编译优化带来的有序性问题**
以为编译优化会改变代码的执行路径。如[双重锁单例]() new 操作,生成对象时执行的方法顺序，导致不同线程间获取的对象不一样。

![](http://media.xindapei.cn/2019-06-30-15618892256926.jpg)

## 3、解决可见性、有序性问题（内存模型）
### [按需禁用](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html)
既然缓存会导致可见性问题、编译优化会导致有序性问题，最直接的办法就行禁用缓存和编译优化。如果全部禁用会导致效率下降，最好的办法是按需禁用。方法包括 volatile、synchronized、final 和六项 Happens-Before 规则。

#### volatile
` volatile int x = 0` 对变量不采用缓存和编译优化
#### synchronized
synchronized 是 java 对管程的实现
#### final
告诉编译器该变量生而不变

#### Happens-Before
如果 A 是 B 发生的原因，那么 A 一定是先于(Happens-Before) B 发生的。无论 A、B 是否在同一个线程。

1. **程序的顺序规则**：程序前面对某个变量的修改一定是对后续操作可见的
  
    ``` java
    class VolatileExample {
      int x = 0;
      volatile boolean v = false;
      public void writer() {
        x = 42;
        v = true;
      }
      public void reader() {
        if (v == true) {
          // 这里 x 会是多少呢？
        }
      }
    }
    ```

2. **volatile 变量规则：**
3. **传递性规则**：如果 A Happens-Before B，且 B Happens-Before C，那么 A Happens-Before C。
4. **管程（synchronized）中锁的规则**：对一个锁的解锁 Happens-Before 于后续对这个锁加锁
    ``` java
    synchronized (this) { // 此处自动加锁
  // x 是共享变量, 初始值 =10
  if (this.x < 12) {
    this.x = 12; 
  }  
} // 此处自动解锁

    ```
5. **线程 start() 规则**：是指主线程 A 启动子线程 B 后，子线程 B 能够看到主线程在启动子线程 B 前的操作。

    ``` java
    Thread B = new Thread(()->{
      // 主线程调用 B.start() 之前
      // 所有对共享变量的修改，此处皆可见
      // 此例中，var==77
    });
    // 此处对共享变量 var 修改
    var = 77;
    // 主线程启动子线程
    B.start();
    ```
6. **线程 join() 规则**：主线程 A 等待子线程 B 完成（主线程 A 通过调用子线程B 的 join() 方法实现），当子线程 B 完成后（主线程 A 中 join() 方法返回），主线程能够看到子线程的操作。

    ``` java
    Thread B = new Thread(()->{
      // 此处对共享变量 var 修改
      var = 66;
    });
    // 例如此处对共享变量修改，
    // 则这个修改结果对线程 B 可见
    // 主线程启动子线程
    B.start();
    B.join()
    // 子线程所有对共享变量的修改
    // 在主线程调用 B.join() 之后皆可见
    // 此例中，var==66
    ```
## 4、解决原子性问题（互斥锁）
### 锁模型
让一段时间只能有一个线程访问，这叫锁

![锁模型](http://media.xindapei.cn/2019-06-30-15619027436336.jpg)

### synchronized


注意：
```
当修饰静态方法的时候，锁定的是当前类的 Class 对象，在上面的例子中就是 Class X；
当修饰非静态方法的时候，锁定的是当前实例对象 this。
```



