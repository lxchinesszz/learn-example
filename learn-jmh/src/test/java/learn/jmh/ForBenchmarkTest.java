package learn.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@Threads(2)
@Fork(1)
@State(Scope.Thread)
public class ForBenchmarkTest {

    private static List<Integer> integers;

    static {
        integers = generate(1000000);
    }

    private static List<Integer> generate(Integer max) {
        List<Integer> result = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            result.add(i);
        }
        return result;
    }

    @Benchmark
    public void forTest() {
        for (int i = 0; i < integers.size(); i++) {
            System.out.println(integers.get(i));
        }
    }

    @Benchmark
    public void forEachTest() {
        for (Integer integer : integers) {
            System.out.println(integer);
        }
    }

    @Benchmark
    public void iteratorTest() {
        Iterator<Integer> iterator = integers.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Benchmark
    public void streamForEachTest() {
        integers.forEach(System.out::println);
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ForBenchmarkTest.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run();
    }
}
