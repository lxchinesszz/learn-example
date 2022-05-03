package learn.classloader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * @author liuxin
 * 2022/5/1 12:42
 */
public class DynamicClassLoader extends ClassLoader {

    /**
     * 要加载的 Java 类的 classpath 路径
     */
    private String classpath;

    public DynamicClassLoader(String classpath) {
        // 指定父加载器
        super(ClassLoader.getSystemClassLoader());
        this.classpath = classpath;
    }

//    @Override
//    protected Class<?> findClass(String name) throws ClassNotFoundException {
//        byte[] data = this.loadClassData(name);
//        return this.defineClass(name, data, 0, data.length);
//    }
//
//    /**
//     * 加载 class 文件中的内容
//     *
//     * @param name 类名
//     * @return byte[]
//     */
//    private byte[] loadClassData(String name) {
//        try {
//            // 传进来是带包名的
//            name = name.replace(".", "//");
//            String filePath = classpath + name + ".class";
//            FileInputStream inputStream = new FileInputStream(filePath);
//            // 定义字节数组输出流
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            int b = 0;
//            while ((b = inputStream.read()) != -1) {
//                baos.write(b);
//            }
//            inputStream.close();
//            return baos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
