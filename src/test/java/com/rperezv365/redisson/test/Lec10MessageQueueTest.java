package com.rperezv365.redisson.test;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDequeReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec10MessageQueueTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 05/09/2025 - 16:27
 * @since 1.17
 */
@Slf4j
public class Lec10MessageQueueTest extends BaseTest {

    private RBlockingDequeReactive<Long> msgQueue;

    @BeforeAll
    public void setupQueue() {
        this.msgQueue = super.client.getBlockingDeque("message-queue", LongCodec.INSTANCE);
    }

    @Test
    public void consumer1() {

        this.msgQueue.takeElements()
                .doOnNext(i -> log.info("Consumer 1: {}", i))
                .doOnError(error -> log.info("{}", error))
                .subscribe();

        super.sleep(600_000);
    }

    @Test
    public void consumer2() {

        this.msgQueue.takeElements()
                .doOnNext(i -> log.info("Consumer 2: {}", i))
                .doOnError(error -> log.info("{}", error))
                .subscribe();

        super.sleep(600_000);
    }

    @Test
    public void producer() {

        Mono<Void> mono = Flux.range(1, 100)
                .delayElements(Duration.ofMillis(500))
                .doOnNext(i -> log.info("Producing: {}", i))
                .flatMap(i -> this.msgQueue.add(Long.valueOf(i)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

    }
}
