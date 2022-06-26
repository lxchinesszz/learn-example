package learn.jvm;

/**
 * @author liuxin
 * 2022/6/1 00:20
 */
public class Main {

    private static int i = 0;

    /**
     * https://www.cnblogs.com/likehua/p/3369823.html
     * -Xmx3550m -Xms3550m -Xmn2g -Xss128k
     *
     * @param args
     */
    public static void main(String[] args) {
        // 模拟堆溢出
        // 模拟非堆溢出
        // 模拟栈溢出
        try {
            deepLoop();
        } catch (StackOverflowError error) {
            System.out.println("栈深度:" + i);
        }
        while (true) ;
    }

    /**
     * -Xss160k. 深度850
     * -Xss320k. 深度2898
     * -Xss640k. 深度6994
     * 不设置,深度11909
     */
    public static void deepLoop() {
        i++;
        deepLoop();
    }
}
