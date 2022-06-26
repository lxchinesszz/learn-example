package learn.reactor;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author liuxin
 * 2022/5/1 22:03
 */
public class FluxTest {

    @Test
    public void testWindow(){
        //一维Flux
        Flux<String> stringFlux1 = Flux.just("a","b","c","d","e","f","g","h","i");
        //二维Flux
        Flux<Flux<String>> stringFlux2 = stringFlux1.window(2);
        stringFlux2.count().subscribe(System.out::println);
        //三维Flux
        Flux<Flux<Flux<String>>> stringFlux3 = stringFlux2.window(3);
        stringFlux3.count().subscribe(System.out::println);
    }

    /**
     * 区别与Reduce。将每次的计算过程都保留
     * 13610
     */
    @Test
    public void testScan() {
        Flux.just(1, 2, 3, 4).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(System.out::print);
    }


    /**
     * 只关注结果: 10
     * reduce: 1+ 2 + 3 + 4 = 10
     */
    @Test
    public void testReduce() {
        Flux.just(1, 2, 3, 4).reduce(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(System.out::print);
    }

    /**
     * merge: 12344567
     */
    @Test
    public void testMerge() {
        Flux.just(1, 2, 3, 4).mergeWith(Flux.just(4, 5, 6, 7)).subscribe(System.out::print);
    }

    /**
     * last只取最后一个
     */
    @Test
    public void testLast() {
        Flux.just(1, 2, 3, 4).last().subscribe(System.out::println);
    }


    @Test
    public void testDefer() {
        Flux.<Integer>defer((Supplier<Publisher<Integer>>) () -> {
            return Flux.just(1, 2, 3, 4);
        });
    }

    /**
     * skip跳过前2个
     */
    @Test
    public void testSkip() {
        Flux.just(1, 2, 3, 4).skip(2).doOnNext(x -> {
            System.out.println("数字:" + x + "将被发射执行");
        }).subscribe(System.out::println);
    }

    /**
     * 提供发射前操作 do开头的
     */
    @Test
    public void testDoOnNext() {
        Flux.just(1, 2, 3, 4).doOnNext(x -> {
            System.out.println("数字:" + x + "将被发射执行");
        }).subscribe(System.out::println);
    }

    /**
     * 延迟2秒
     *
     * @throws InterruptedException
     */
    @Test
    public void testFluxTime() throws InterruptedException {
        Flux.just(1, 2, 3, 4).delayElements(Duration.ofSeconds(2)).subscribe(System.out::println);
        Thread.currentThread().join();
    }


    /**
     * buffer 用于将数据,分组
     */
    @Test
    public void testFluxBuff() {
        /**
         * [1, 2]
         * [3, 4]
         * [5]
         */
        Flux.just(1, 2, 3, 4, 5).buffer(2, 2).subscribe(System.out::println);

        /**
         * [1, 2, 3]
         * [4]
         */
        Flux.just(1, 2, 3, 4).buffer(3).subscribe(System.out::println);
    }

    @Test
    @DisplayName("Flux Push模式")
    public void testFluxPush() {
        Flux.create((Consumer<FluxSink<Integer>>) sink -> {
            int k = (int) (Math.random() * 10);
            sink.next(k);
        }).subscribe(new Subscriber<Integer>() {

            Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                this.subscription.request(1);
            }

            @SneakyThrows
            @Override
            public void onNext(Integer integer) {
                System.out.println("处理:" + integer);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("处理失败");
            }

            @Override
            public void onComplete() {
                System.out.println("处理完成");
            }
        });
    }

    @Test
    @DisplayName("Flux Pull模式 Integer.MAX_VALUE")
    public void testFluxPull() {
        Flux.generate((Consumer<SynchronousSink<Integer>>) sink -> {
                    int k = (int) (Math.random() * 10);
                    sink.next(k);
                })
                // 默认获取 request(Integer.MAX_VALUE)
                .subscribe(integer -> System.out.println("Pull:" + integer));
    }

    @Test
    @DisplayName("Flux Pull模式 request调用一次,则调用Sink生产一次")
    public void testFluxPullTwo() {
        Flux.generate((Consumer<SynchronousSink<Integer>>) sink -> {
                    int k = (int) (Math.random() * 10);
                    sink.next(k);
                })
                .subscribe(new Subscriber<Integer>() {
                    Subscription subscription;

                    private int count;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.subscription = s;
                        // 订阅时候,生产1条数据
                        this.subscription.request(1);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        count++;
                        System.out.println("处理:" + integer);
                        // 在处理1次，当第二次处理时候,就不拉数据了
                        if (count < 2) {
                            this.subscription.request(1);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

}
