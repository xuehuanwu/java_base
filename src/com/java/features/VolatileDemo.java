package com.java.features;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile是java虚拟机提供的轻量级的同步机制
 * 1、保证可见性：各个线程访问共享变量时，不会直接访问，而是将共享变量读取到各自的工作内存中，然后对变量进行操作，操作完成后再将变量写回主内存，并通知其他线程主内存中的变量已修改。
 * 2、不保证原子性
 * 3、禁止指令重排
 * =====================================================================================================================
 * JVM(Java Virtual Machine) java虚拟机
 * =====================================================================================================================
 * JMM(Java Memory Model) java内存模型，是抽象概念，并不真实存在，是一组规范，规定所有变量都存储在主内存。
 * 1、可见性
 * 2、原子性
 * 3、有序性
 * =====================================================================================================================
 * 多线程环境中线程交替执行，由于编译器优化重排的存在，两个线程中使用的变量能否保证一致性是无法确定的，结果无法预测
 * =====================================================================================================================
 * Memory Barrier 内存屏障，又称内存栅栏，是一个CPU指令，它的作用有两个：
 * 1、保证特定操作的执行顺序
 * 2、保证某些变量的内存可见性(利用该特性实现volatile的内存可见性)
 * =====================================================================================================================
 * 通过插入内存屏障禁止在内存屏障前后的指令执行重排序优化
 * =====================================================================================================================
 * 工作内存与主内存同步延迟现象导致的可见性问题？
 * 可以使用synchronize或volatile关键字解决，它们都可以使一个线程修改后的变量立即对其他线程可见。
 * =====================================================================================================================
 * 对于指令重排导致的可见性问题和有序性问题？
 * 可以利用volatile关键字解决，因为volatile的另外一个作用就是禁止重排序优化。
 */
public class VolatileDemo {

    public static void main(String[] args) {
        visibility(); // 可见性
        atomicity(); // 原子性
    }

    /**
     * 1、验证volatile的可见性
     * 1.1 假如 int number = 0; number变量之前根本没有添加volatile关键字修改，没有可见性
     * 1.2 添加了volatile，可以解决可见性问题
     * <p>
     * volatile可以保证可见性，及时通知其它线程，主物理内存的值已经被修改。
     */
    public static void visibility() {
        MyData myData = new MyData(); // 资源类

        // 第1个线程修改number的值
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                TimeUnit.SECONDS.sleep(3); // 暂停3秒
            } catch (Exception e) {
                e.getStackTrace();
            }
            myData.addT060();
            System.out.println(Thread.currentThread().getName() + "\t update number value:" + myData.number);
        }, "AAA").start();

        // 第2个线程就是我们的main线程
        while (myData.number == 0) {
            // main线程就一直在这里等待循环，直到number值不再等于0
        }

        System.out.println(Thread.currentThread().getName() + "\t mission is over");
    }

    /**
     * 2、验证volatile不保证原子性
     * 2.1 原子性指的是什么意思？
     * 不可分割，完整性，也即某个线程正在做的某个具体业务时，中间不可以被加塞或者被分割。要么同时成功，要么同时失败。
     * 2.2 volatile不保证原子性，案例演示
     * 2.3 why
     * 出现丢失写值情况
     * 2.4 如何解决原子性
     * 加sync
     * 使用我们的juc下AtomicInteger
     */
    public static void atomicity() {
        MyData myData = new MyData();

        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    myData.addPlusPlus(); // 不保证原子性
                    myData.addMyAtomic(); // 保证原子性
                }
            }, String.valueOf(i)).start();
        }

        // 需要等待上面20个线程都全部计算完成后，再用main线程取得最终的结果值看是多少？
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "\t int type, finally number value: " + myData.number);
        System.out.println(Thread.currentThread().getName() + "\t AtomicInteger type, finally number value: " + myData.atomicInteger);
    }
}

// MyData.java --> MyData.class --> jvm字节码
class MyData {
    volatile int number = 0;

    public void addT060() {
        this.number = 60;
    }

    // 注意，此时number前面是加了volatile关键字修饰的
    public void addPlusPlus() {
        number++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();

    public void addMyAtomic() {
        atomicInteger.getAndIncrement();
    }
}