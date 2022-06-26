package learn.jvm;


/**
 * @author liuxin
 * 2022/6/3 01:55
 */
public class CPU {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {

                }
            }).start();
        }
    }
}
