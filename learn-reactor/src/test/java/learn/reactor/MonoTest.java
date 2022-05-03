package learn.reactor;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

/**
 * @author liuxin
 * 2022/5/1 22:00
 */
public class MonoTest {

    @Test
    @DisplayName("Mono 事件驱动的好处")
    public void testMono() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("基于事件驱动的编程思想");
        Mono<String> userNameMono = getMonoUserName();
        stopWatch.stop();
        stopWatch.start("传统阻塞式的编程思想");
        System.out.println(getUserName());
        stopWatch.stop();
        System.out.println(userNameMono.block());
        System.out.println(stopWatch.prettyPrint());
    }


    @SneakyThrows
    public String getUserName() {
        Thread.sleep(2000L);
        return "JayChou";
    }

    /**
     * 基于事件驱动的编程思想
     *
     * @return Mono<String>
     */
    public Mono<String> getMonoUserName() {
        return Mono.create(monoSink -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                monoSink.error(new RuntimeException(e));
                return;
            }
            monoSink.success("JayChou");
        });
    }

}
