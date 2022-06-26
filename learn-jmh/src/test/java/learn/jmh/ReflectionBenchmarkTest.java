package learn.jmh;

import learn.common.print.Loops;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxin
 * 2022/5/28 13:59
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Threads(2)
@Fork(1)
@State(Scope.Thread)
public class ReflectionBenchmarkTest {

    private static class Mode {
        private Integer age;

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    @Benchmark
    public void test1k() {
        Mode mode = new Mode();
        Loops.loop(1000, mode::setAge);
    }

    @Benchmark
    public void testReflection1k() {
        Mode mode = new Mode();
        Loops.loop(1000, i -> {
            Method setAge = null;
            try {
                setAge = mode.getClass().getMethod("setAge", Integer.class);
                setAge.invoke(mode, i);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Benchmark
    public void testReflectionAccessible1k() {
        Mode mode = new Mode();
        Loops.loop(1000, i -> {
            Method setAge = null;
            try {
                setAge = mode.getClass().getMethod("setAge", Integer.class);
                setAge.setAccessible(true);
                setAge.invoke(mode, i);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Benchmark
    public void test1w() {
        Mode mode = new Mode();
        Loops.loop(10000, mode::setAge);
    }

    @Benchmark
    public void testReflection1w() {
        Mode mode = new Mode();
        Loops.loop(10000, i -> {
            Method setAge = null;
            try {
                setAge = mode.getClass().getMethod("setAge", Integer.class);
                setAge.invoke(mode, i);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Benchmark
    public void testReflectionAccessible1w() {
        Mode mode = new Mode();
        Loops.loop(10000, i -> {
            Method setAge = null;
            try {
                setAge = mode.getClass().getMethod("setAge", Integer.class);
                setAge.setAccessible(true);
                setAge.invoke(mode, i);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Benchmark
    public void metaClass1k() {
        MetaClass metaClass = MetaClass.forClass(Mode.class, new DefaultReflectorFactory());
        Mode mode = new Mode();
        Loops.loop(10000, i -> {
            try {
                Invoker setAge = metaClass.getSetInvoker("age");
                setAge.invoke(mode, new Object[]{i});
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Benchmark
    public void metaClass1w() {
        MetaClass metaClass = MetaClass.forClass(Mode.class, new DefaultReflectorFactory());
        Mode mode = new Mode();
        Loops.loop(10000, i -> {
            try {
                Invoker setAge = metaClass.getSetInvoker("age");
                setAge.invoke(mode, new Object[]{i});
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }


    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ReflectionBenchmarkTest.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run();
    }
}
