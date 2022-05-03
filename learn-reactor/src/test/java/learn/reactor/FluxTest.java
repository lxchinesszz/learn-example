package learn.reactor;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SynchronousSink;

import java.util.function.Consumer;

/**
 * @author liuxin
 * 2022/5/1 22:03
 */
public class FluxTest {

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
