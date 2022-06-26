package learn.jvm.oom;

import learn.common.print.Sleeps;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 字节码手册
 * 几个oom示例
 * https://blog.csdn.net/u012260238/article/details/110308147
 * 虚拟机栈
 * 一个线程一个虚拟机栈
 * 一个虚拟机栈中有几个栈帧
 * <p>
 * TLAB(thread local allocate buffer)
 * - 线程私有堆(新生代)
 * PLAB
 * - 线程私有堆(老年代)
 * <p>
 * 栈帧
 * - 局部变量表
 * - 存储变量的表
 * - 操作数栈
 * <p>
 * - 动态链接
 * - Java类方法对应的C++对象在方法区的内存地址
 * - 返回地址
 * - 存储的是调用方的程序技术器
 * - add方法存储的是main方法的程序计数器，当执行完add方法后，要知道跳到main方法的哪里。
 * <p>
 * <p>
 * 0 new #2 <learn/jvm/Test>
 * - 在堆里生成一个不完全对象(未执行构造方法)
 * - 将不完全对象的指针压入栈
 * 3 dup
 * - 赋值栈顶元素
 * - 再次压入栈
 * 4 invokespecial(操作数栈) #3 <learn/jvm/Test.<init> : ()V>
 * - 给this赋值
 * 7 astore_1
 * pop栈，给demo赋值
 *
 * @author liuxin
 * 2022/6/5 01:08
 */
public class MetaspaceOverflowTest {

    /**
     * 查看元空间配置
     * java -XX:+PrintFlagsFinal -version | grep Metaspace
     * 方法区是JVM规范。
     * - 永久代和元空间是实现
     * 元空间调优规则:
     * 1. 最大最小设置成一样大
     * 防止内存抖动
     *
     * @param args -XX:MetaspaceSize=20m
     *             -XX:MaxMetaspaceSize=20m
     *             java.lang.OutOfMemoryError-->Metaspace
     */
    public static void main(String[] args) {
//        while (true) {
            Sleeps.sleep(0.2);
            for (int i = 0; i < 200; i++) {
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(MetaspaceOverflowTest.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        return methodProxy.invokeSuper(objects, args);
                    }
                });
                System.out.println("create InstanceKlass...");
                enhancer.create();
            }
//        }
        while (true);
        //  java.lang.OutOfMemoryError-->Metaspace
    }
}
