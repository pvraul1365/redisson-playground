package com.rperezv365.redisson.test;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec01KeyValueTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 30/08/2025 - 18:37
 * @since 1.17
 */
@Slf4j
public class Lec01KeyValueTest extends BaseTest {

    @Test
    public void keyValueTest() {
        RBucketReactive<String> bucket = super.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam");
        Mono<Void> get = bucket.get()
                .doOnNext(log::info)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueExpireTest() {
        RBucketReactive<String> bucket = super.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", Duration.ofSeconds(10));
        Mono<Void> get = bucket.get()
                .doOnNext(log::info)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueExtendsExpireTest() {
        RBucketReactive<String> bucket = super.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", Duration.ofSeconds(10));
        Mono<Void> get = bucket.get()
                .doOnNext(log::info)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

        // extends expiration
        sleep(5000);
        Mono<Boolean> mono = bucket.expire(Duration.ofSeconds(60));
        StepVerifier.create(mono)
                .expectNext(true)
                .verifyComplete();

        // access expiration time
        Mono<Void> ttl = bucket.remainTimeToLive()
                .doOnNext(time -> log.info("TTL: {}", time))
                .then();

        StepVerifier.create(ttl)
                .verifyComplete();

    }

}
