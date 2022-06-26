package learn.jvm.oom;

/**
 * @author liuxin
 * 2022/6/5 18:56
 */
public class StackOverflowTest {

    /**
     * Exception in thread "main" java.lang.StackOverflowError
     * @param args
     */
    public static void main(String[] args) {
        StackOverflowTest stackOverflowTest = new StackOverflowTest();
        stackOverflowTest.mockStackOverflow();
    }

    public void mockStackOverflow() {
        mockStackOverflow();
    }
}
