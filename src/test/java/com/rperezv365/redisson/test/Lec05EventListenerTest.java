package com.rperezv365.redisson.test;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec05EventListenerTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 31/08/2025 - 14:05
 * @since 1.17
 */
@Slf4j
public class Lec05EventListenerTest extends BaseTest {

    @Test
    public void expiredEventListenerTest() {
        RBucketReactive<String> bucket = super.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", Duration.ofSeconds(10));
        Mono<Void> get = bucket.get()
                .doOnNext(log::info)
                .then();

        Mono<Void> event = bucket.addListener((ExpiredObjectListener) s -> log.info("Expired: {}", s)).then();

        StepVerifier.create(set.concatWith(get).concatWith(event))
                .verifyComplete();

        // extends expiration
        sleep(11000);


    }

}
