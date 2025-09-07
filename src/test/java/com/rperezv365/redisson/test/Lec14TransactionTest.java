package com.rperezv365.redisson.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RTransactionReactive;
import org.redisson.api.TransactionOptions;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec14TransactionTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 07/09/2025 - 09:12
 * @since 1.17
 */
@Slf4j
public class Lec14TransactionTest extends BaseTest {

    private RBucketReactive<Long> user1Balance;
    private RBucketReactive<Long> user2Balance;

    @BeforeAll
    public void accountSetup() {
        this.user1Balance = super.client.getBucket("user:1:balance", LongCodec.INSTANCE);
        this.user2Balance = super.client.getBucket("user:2:balance", LongCodec.INSTANCE);
        Mono<Void> mono = user1Balance.set(100L)
                .then(user2Balance.set(0L))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @AfterAll
    public void accountBalanceSatus() {
        Mono<Void> mono = Flux.zip(this.user1Balance.get(), this.user2Balance.get())
                .doOnNext(t -> log.info("User 1 balance: {}, User 2 balance: {}", t.getT1(), t.getT2()))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void nonTransactional() {
        this.transfer(50, this.user1Balance, this.user2Balance)
                .thenReturn(0)
                .map(i -> 5 / i) // some error
                .doOnError(e -> log.error("Error in transaction: {}", e.getMessage()))
                .subscribe();

        sleep(1_000);
    }

    @Test
    public void transactional() {
        RTransactionReactive transaction = super.client.createTransaction(TransactionOptions.defaults());
        RBucketReactive<Long> user1Balance = transaction.getBucket("user:1:balance", LongCodec.INSTANCE);
        RBucketReactive<Long> user2Balance = transaction.getBucket("user:2:balance", LongCodec.INSTANCE);

        this.transfer(50, user1Balance, user2Balance)
                .thenReturn(0)
                .map(i -> 5 / i) // some error
                .then(transaction.commit())
                .doOnError(e -> log.error("Error in transaction: {}", e.getMessage()))
                .onErrorResume(e -> transaction.rollback())
                .subscribe();

        sleep(1_000);
    }

    private Mono<Void> transfer(int amount, RBucketReactive<Long> from, RBucketReactive<Long> to) {
        return Flux.zip(from.get(), to.get()) // [b1, b2]
                .filter(t -> t.getT1() >= amount)
                .flatMap(t -> from.set(t.getT1() - amount).thenReturn(t))
                .flatMap(t -> to.set(t.getT2() + amount))
                .then();
    }

}
