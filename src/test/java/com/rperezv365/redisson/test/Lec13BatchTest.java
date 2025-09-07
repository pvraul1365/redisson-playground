package com.rperezv365.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec13BatchTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 07/09/2025 - 08:50
 * @since 1.17
 */
public class Lec13BatchTest extends BaseTest {

    @Test // 3 seconds
    public void batchTest() {

        RBatchReactive batch = super.client.createBatch(BatchOptions.defaults());
        RListReactive<Long> list = batch.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);

        for (long i = 0; i < 500_000; i++) {
            list.add(i);
            set.add(i);
        }
        StepVerifier.create(batch.execute().then())
                .verifyComplete();
    }

    @Test // 17 seconds
    public void regularTest() {

        RListReactive<Long> list = super.client.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = super.client.getSet("numbers-set", LongCodec.INSTANCE);

        Mono<Void> mono = Flux.range(1, 500_000)
                .map(Long::valueOf)
                .flatMap(i -> list.add(i).then(set.add(i)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

}
