package com.rperezv365.redisson.test;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLongReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec03NumberTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 31/08/2025 - 12:05
 * @since 1.17
 */
public class Lec03NumberTest extends BaseTest {

    @Test
    public void keyValueIncreaseTest() {
        // set k v -- incr, decr
        RAtomicLongReactive atomicLongReactive = super.client.getAtomicLong("user:1:visit");
        Mono<Void> mono = Flux.range(1, 30)
                        .delayElements(Duration.ofSeconds(1))
                        .flatMap(i -> atomicLongReactive.incrementAndGet())
                                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

}
