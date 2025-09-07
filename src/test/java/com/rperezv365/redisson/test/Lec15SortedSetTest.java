package com.rperezv365.redisson.test;

import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec15SortedSetTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 07/09/2025 - 11:19
 * @since 1.17
 */
@Slf4j
public class Lec15SortedSetTest extends BaseTest {

    @Test
    public void sortedSetTest() {
        RScoredSortedSetReactive<Object> sortedSet = super.client.getScoredSortedSet("student:score", StringCodec.INSTANCE);
        Mono<Void> mono = sortedSet.addScore("sam", 12.25)
                .then(sortedSet.add(23.25, "mike"))
                .then(sortedSet.addScore("jake", 7))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        sortedSet.entryRange(0, 1)
                .flatMapIterable(Function.identity()) // flux
                .map(se -> "Value: " + se.getValue() + " Score: " + se.getScore())
                .doOnNext(log::info)
                .subscribe();

        sleep(1_000);
    }

}
