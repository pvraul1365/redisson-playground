package com.rperezv365.redisson.test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec11HyperLogLogTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 05/09/2025 - 17:53
 * @since 1.17
 */
@Slf4j
public class Lec11HyperLogLogTest extends BaseTest {

    @Test
    public void count() {
        RHyperLogLogReactive<Long> counter = super.client.getHyperLogLog("user:visits", LongCodec.INSTANCE);

        List<Long> longList1 = LongStream.rangeClosed(1, 25000)
                .boxed()
                .collect(Collectors.toList());

        List<Long> longList2 = LongStream.rangeClosed(25001, 50000)
                .boxed()
                .collect(Collectors.toList());

        List<Long> longList3 = LongStream.rangeClosed(1, 75000)
                .boxed()
                .collect(Collectors.toList());

        List<Long> longList4 = LongStream.rangeClosed(50000, 100_000)
                .boxed()
                .collect(Collectors.toList());

        Mono<Void> mono = Flux.just(longList1, longList2, longList3, longList4)
                .flatMap(counter::addAll)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        counter.count()
                .doOnNext(c -> log.info("Count: {}", c))
                .subscribe();
    }

}
