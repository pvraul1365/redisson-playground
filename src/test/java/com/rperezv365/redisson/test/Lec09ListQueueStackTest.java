package com.rperezv365.redisson.test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec09ListQueueStackTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 04/09/2025 - 18:59
 * @since 1.17
 */
@Slf4j
public class Lec09ListQueueStackTest extends BaseTest {

    @Test
    public void listTest() {
        // lrange numnber-input 0 -1
        RListReactive<Long> list = super.client.getList("number-input", LongCodec.INSTANCE);

        List<Long> longList = LongStream.rangeClosed(1, 10)
                .boxed()
                .collect(Collectors.toList());

        StepVerifier.create(list.addAll(longList).then())
                .verifyComplete();

        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();

    }

    @Test
    public void queueTest() {
        // lrange numnber-input 0 -1
        RQueueReactive<Long> queue = super.client.getQueue("number-input", LongCodec.INSTANCE);

        Mono<Void> queuePoll = queue.poll()
                .repeat(3)
                .doOnNext(i -> log.info("Polled: {}", i))
                .then();

        StepVerifier.create(queuePoll)
                .verifyComplete();

        StepVerifier.create(queue.size())
                .expectNext(6)
                .verifyComplete();

    }

    @Test
    public void stackTest() {
        // lrange numnber-input 0 -1
        RDequeReactive<Long> deque = super.client.getDeque("number-input", LongCodec.INSTANCE);

        Mono<Void> stackPoll = deque.pollLast()
                .repeat(3)
                .doOnNext(i -> log.info("Polled: {}", i))
                .then();


        StepVerifier.create(stackPoll)
                .verifyComplete();

        StepVerifier.create(deque.size())
                .expectNext(2)
                .verifyComplete();

    }

}
