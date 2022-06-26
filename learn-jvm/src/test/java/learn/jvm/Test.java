package learn.jvm;

/**
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
 * 2022/6/5 01:49
 */
public class Test {
    public static void main(String[] args) {
        Test demo = new Test();
        System.out.println(demo.add(1, 2));
    }

    public int add(int a, int b) {
        return a + b;
    }
}
