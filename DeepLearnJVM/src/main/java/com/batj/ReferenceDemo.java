package com.batj;

/**
 * @author:yeluo
 */
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceDemo {
    
    private int testID1;
    private String testName1;

    private int testID2;
    private String testName2;

    /**
     * 运行环境:64bit windows
     * JDK版本:JDK6
     */
    public static void main(String[] args) {
//		softRefTest();
//		weakRefTest();
        phantomRefTest();
    }

    public static void phantomRefTest() {
        Object phantomObj = new Object();
        ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
        PhantomReference<Object> phantomRef = new PhantomReference<Object>(phantomObj, queue);
        phantomObj = null; // 将对象和强引用关联取消掉
        System.err.println("虚引用关联的对象是否存在呢 : " + phantomRef.get()); // 这里不管什么时候获取都是null
        System.err.println("B4 GC 虚引用关联的对象是否存在呢 : " + queue.poll());
        System.err.println("来一次GC");
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("A5 GC 虚引用关联的对象是否存在呢 : " + queue.poll()); // 说明虚引用关联的对象下一次GC将会被回收
        System.err.println("再来一次GC");
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("Final A5 GC 虚引用关联的对象是否存在呢 : " + queue.poll());
        /**
         * 运行结果:
         * 虚引用关联的对象是否存在呢 : null
         * B4 GC 虚引用关联的对象是否存在呢 : null
         * 来一次GC
         * A5 GC 虚引用关联的对象是否存在呢 : java.lang.ref.PhantomReference@252f0999
         * 再来一次GC
         * Final A5 GC 虚引用关联的对象是否存在呢 : null
         *
         * 通过运行结果说明了,
         * 经过两次GC才会将虚引用关联的对象回收
         */
    }

    public static void weakRefTest() {
        Object weakObj = new Object();
        WeakReference<Object> weakRef = new WeakReference<Object>(weakObj);
        weakObj = null; // 将对象和强引用关联取消掉
        System.err.println("B4 GC 弱引用关联的对象是否存在呢 : " + weakRef.get());
        System.err.println("来一次GC");
        System.gc();
        System.err.println("A5 GC 弱引用关联的对象是否存在呢 : " + weakRef.get());
        /**
         * 运行结果:
         * B4 GC 弱引用关联的对象是否存在呢 : java.lang.Object@24e2dae9
         * 来一次GC
         * A5 GC 弱引用关联的对象是否存在呢 : null
         *
         * 通过运行结果说明了,
         * GC会将弱引用关联的对象回收,即该对象创建之后, 面对即将来临的GC毫无抵抗力(不堪一击)
         */
    }

    public static void softRefTest() {
        Object softObj = new Object();
        SoftReference<Object> softRef = new SoftReference<Object>(softObj);
        softObj = null; // 将对象和强引用关联取消掉
        System.err.println("B4 GC 软引用关联的对象是否存在呢 : " + softRef.get());
        System.err.println("来一次GC");
        System.gc();
        System.err.println("A5 GC 软引用关联的对象是否存在呢 : " + softRef.get());
        /**
         * 运行结果:
         * B4 GC 软引用关联的对象是否存在呢 : java.lang.Object@670655dd
         * 来一次GC
         * A5 GC 软引用关联的对象是否存在呢 : java.lang.Object@670655dd
         *
         * 通过运行结果说明了,
         * 在JVM没有报OOM异常前, 即使GC也不会将软引用关联的对象回收
         */
    }

}
