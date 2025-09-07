package com.rperezv365.redisson.test.assignment;

import org.redisson.api.RScoredSortedSetReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * PriorityQueue
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 07/09/2025 - 12:01
 * @since 1.17
 */
public class PriorityQueue {

    RScoredSortedSetReactive<UserOrder> queue;

    public PriorityQueue(RScoredSortedSetReactive<UserOrder> queue) {
        this.queue = queue;
    }

    public Mono<Void> add(UserOrder order) {
        return this.queue.add(
                order.getCategory().ordinal(),
                order
        ).then();
    }

    public Flux<UserOrder> takeItems() {
        return this.queue.takeFirstElements()
                .limitRate(1);
    }

}
