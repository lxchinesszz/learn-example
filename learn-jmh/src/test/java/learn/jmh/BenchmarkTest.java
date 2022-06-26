package learn.jmh;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/xiandafu/article/details/94029094
 *
 * @author liuxin
 * 2022/5/24 22:20
 */
public class BenchmarkTest {
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 2, time = 3)
    @Measurement(iterations = 2, time = 2)
    @Threads(2)
    @Fork(1)
    public void test() {
        loopTest();
    }

    // http://deepoove.com/jmh-visual-chart/ 可视化分析
    // https://jmh.morethan.io/ 推荐
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(BenchmarkTest.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run();
    }

    public void loopTest() {
        for (int i = 0; i < 1000; i++) {

        }
    }
}
