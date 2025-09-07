package com.rperezv365.redisson.test;

import com.rperezv365.redisson.test.assignment.Category;
import com.rperezv365.redisson.test.assignment.PriorityQueue;
import com.rperezv365.redisson.test.assignment.UserOrder;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec16PriorityQueueTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 07/09/2025 - 12:09
 * @since 1.17
 */
@Slf4j
public class Lec16PriorityQueueTest extends BaseTest {

    private PriorityQueue priorityQueue;

    @BeforeAll
    public void setupQueue() {
        RScoredSortedSetReactive<UserOrder> sortedSet = super.client.getScoredSortedSet("user:order:queue", new TypedJsonJacksonCodec(UserOrder.class));
        this.priorityQueue = new PriorityQueue(sortedSet);
    }

    @Test
    public void producer1() {
        UserOrder order1 = UserOrder.builder().id(1).category(Category.GUEST).build();
        UserOrder order2 = UserOrder.builder().id(2).category(Category.STD).build();
        UserOrder order3 = UserOrder.builder().id(3).category(Category.PRIME).build();
        UserOrder order4 = UserOrder.builder().id(4).category(Category.STD).build();
        UserOrder order5 = UserOrder.builder().id(5).category(Category.GUEST).build();

        Mono<Void> mono = Flux.just(order1, order2, order3, order4, order5)
                .flatMap(this.priorityQueue::add)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void producer() {
        Flux.interval(Duration.ofSeconds(1))
                .map(l -> (l.intValue() * 5))
                .doOnNext(i -> {
                    UserOrder order1 = UserOrder.builder().id(i + 1).category(Category.GUEST).build();
                    UserOrder order2 = UserOrder.builder().id(i + 2).category(Category.STD).build();
                    UserOrder order3 = UserOrder.builder().id(i + 3).category(Category.PRIME).build();
                    UserOrder order4 = UserOrder.builder().id(i + 4).category(Category.STD).build();
                    UserOrder order5 = UserOrder.builder().id(i + 5).category(Category.GUEST).build();

                    Mono<Void> mono = Flux.just(order1, order2, order3, order4, order5)
                            .flatMap(this.priorityQueue::add)
                            .then();

                    StepVerifier.create(mono)
                            .verifyComplete();

                }).subscribe();

        sleep(60_000);
    }

    @Test
    public void consumer() {
        this.priorityQueue.takeItems()
                .delayElements(Duration.ofMillis(500))
                .doOnNext(order -> log.info("Processing order: {}", order))
                .subscribe();
        sleep(600_000);
    }

}
