package learn.classloader;

import learn.common.print.ColorConsole;
import lombok.SneakyThrows;

/**
 * @author liuxin
 * 2022/5/1 13:16
 */
public class School {

    @SneakyThrows
    public void radio() {
        while (true) {
            Thread.sleep(3000L);
            ColorConsole.colorPrintln("起来做核酸了");
            DynamicClassLoader dynamicClassLoader = new DynamicClassLoader("/Users/liuxin/Github/learn-example/learn-classloader/target/classes/learn/classloader");
//            dynamicClassLoader.findClass("learn.classloader.School");
        }
    }
}
