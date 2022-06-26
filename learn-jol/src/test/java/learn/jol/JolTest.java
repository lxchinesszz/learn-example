package learn.jol;

import learn.common.print.ColorConsole;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import java.util.concurrent.TimeUnit;

/**
 * 1. 偏向锁 和 轻量级锁都是用户空间锁
 * 2. 重量级锁是要向内核申请的
 * <p>
 * <p>
 * 如果是单线程的，就不要考虑用锁。
 *
 * @author liuxin
 * 2022/5/28 14:53
 */
public class JolTest {

    private static class User {
    }


    /**
     * 非数组
     *   InstanceKlass       Class的元信息,在方法区(Metaspace)
     *   InstanceMirrorKlass Class对象在堆区
     * 数组
     *   TypeArrayKlass(基本类型数组)
     *   ObjArrayKlass(引用类型数组)
     *
     * Klass Pointer 类型指针
     *
     * 对象头: Markword, Klass Pointer类型指针,数组长度(数组类型才有),Padding不是8的倍数就填充
     */
    @Test
    public void test() {
        User user = new User();
        //查看对象的内存布局
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }

    /**
     * 第一个获取锁的线程通过CAS将自己的threadId写入到该对象的mark word中.
     * 若后续该线程再次获取锁，需要比较当前线程threadId和对象mark word中的threadId是否一致，
     * 如果一致那么可以直接获取，并且锁对象始终保持对该线程的偏向，也就是说偏向锁不会主动释放
     */
    @Test
    public void test2() {
        User user = new User();
        synchronized (user) {
            System.out.println(ClassLayout.parseInstance(user).toPrintable());
        }
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
        synchronized (user) {
            System.out.println(ClassLayout.parseInstance(user).toPrintable());
        }
    }

    /**
     * 当两个或以上线程交替获取锁，但并没有在对象上并发的获取锁时，偏向锁升级为轻量级锁。
     *
     * @throws InterruptedException 中断异常
     */
    @Test
    public void test3() throws InterruptedException {
        User user = new User();
        // 没有人竞争就是偏向锁
        // 第一个获取锁的线程通过CAS将自己的threadId写入到该对象的mark word中.
        // 若后续该线程再次获取锁，需要比较当前线程threadId和对象mark word中的threadId是否一致，如果一致那么可以直接获取，并且锁对象始终保持对该线程的偏向，也就是说偏向锁不会主动释放
        synchronized (user) {
            System.out.println("--MAIN--:" + ClassLayout.parseInstance(user).toPrintable());
        }

        // 有线程竞争,升级为轻量级锁
        Thread thread = new Thread(() -> {
            synchronized (user) {
                System.out.println("--THREAD--:" + ClassLayout.parseInstance(user).toPrintable());
            }
        });
        thread.start();
        thread.join();
        // 竞争完整恢复成无锁状态
        System.out.println("--END--:" + ClassLayout.parseInstance(user).toPrintable());

    }


    /**
     * 当有并发获取对象锁,则会升级为重量级锁
     * 可以看到，在两个线程同时竞争user对象的锁时，会升级为10重量级锁。
     */
    @Test
    @SneakyThrows
    public void test4() {
        User user = new User();
        new Thread(() -> {
            synchronized (user) {
                System.out.println("--THREAD1--:" + ClassLayout.parseInstance(user).toPrintable());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (user) {
                System.out.println("--THREAD2--:" + ClassLayout.parseInstance(user).toPrintable());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.currentThread().join();
    }


    @Test
    public void hashCodeTest() {
        User user = new User();
        //打印内存布局
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
        //计算hashCode
        System.out.println(user.hashCode());
        //再次打印内存布局
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }

    public static void main(String[] args) {
        User obj = new User();
//        相关方法：
//        1.使用jol计算对象的大小（单位为字节）：
        ColorConsole.colorPrintln("对象的大小(bit):{}", ClassLayout.parseInstance(obj).instanceSize());
        System.out.println("----------------------------");
//        2.使用jol查看对象内部的内存布局：
//        - OFFSET：偏移地址，单位字节
//        - SIZE：占用的内存大小，单位为字节
//        - TYPE DESCRIPTION： 类型描述，其中object header为对象头；
//          - object header：对象头
//          - loss due to the next object alignment：由于下一个对象对齐而导致的丢失.补齐位置。可有可无，若对象头加上实例数据是8的倍数时，则不存在字节对齐
//        - VALUE : 对应内存中当前存储的值；
//        - Instance size：实例字节数值大小。User是空对象,就16byte

        ColorConsole.colorPrintln("内存布局:{}", ClassLayout.parseInstance(obj).toPrintable());
        System.out.println("----------------------------");

//        3.查看对象外部信息：包括引用的对象：
        ColorConsole.colorPrintln("包括引用的对象:{}", GraphLayout.parseInstance(obj).toPrintable());
        System.out.println("----------------------------");

//        4.查看对象占用空间总大小：
        ColorConsole.colorPrintln("查看对象占用空间总大小:{}", GraphLayout.parseInstance(obj).totalSize());


    }
}
