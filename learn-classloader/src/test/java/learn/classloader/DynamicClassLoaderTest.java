package learn.classloader;

import org.junit.jupiter.api.Test;
import sun.misc.Launcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @author liuxin
 * 2022/5/1 12:45
 */
class DynamicClassLoaderTest {

    private Map<String, Long> class2LoadAsMap = new HashMap<>();

    public static void main(String[] args) {
        Thread.currentThread().setContextClassLoader(new DynamicClassLoader(""));
        System.out.println(School.class.getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
    }
    @Test
    void loadClass() throws Exception {
        // java -Djava.system.class.loader=learn.classloader.DynamicClassLoader
        Properties properties = System.getProperties();
        System.clearProperty("user.dir");
        Launcher.getLauncher().getClassLoader();
        School school = new School();
        ClassLoader classLoader = school.getClass().getClassLoader();
        System.out.println(classLoader);
        new Thread(() -> {
            school.radio();
        }).start();

        System.out.println(Thread.currentThread().getContextClassLoader().getClass());
    }
}