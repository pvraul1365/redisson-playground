package com.rperezv365.redisson.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec04BucketAsMapTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 31/08/2025 - 12:28
 * @since 1.17
 */
@Slf4j
public class Lec04BucketAsMapTest extends BaseTest {

    @Test
    public void bucketAsMapTest() {
        // user:1:name
        // user:2:name
        // user:3:name
        Mono<Void> mono = super.client.getBuckets(StringCodec.INSTANCE)
                .get("user:1:name", "user:2:name", "user:3:name")
                .doOnNext(map -> log.info("Received map: {}", map))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

    }

}
