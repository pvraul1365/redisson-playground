package com.rperezv365.redisson.test;

import com.rperezv365.redisson.test.dto.Student;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec07MapCacheTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 02/09/2025 - 16:20
 * @since 1.17
 */
@Slf4j
public class Lec07MapCacheTest extends BaseTest {

    @Test
    public void mapCacheTest() {
        // Map<Integer, Student>
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapCacheReactive<Integer, Student> mapCache = super.client.getMapCache("users:cache", codec);

        Student student1 = Student.builder()
                .name("sam")
                .age(10)
                .city("atlanta")
                .marks(List.of(1, 2, 3))
                .build();

        Student student2 = Student.builder()
                .name("jake")
                .age(30)
                .city("miami")
                .marks(List.of(10, 20, 30))
                .build();

        Mono<Student> st1 = mapCache.put(1, student1, 2, TimeUnit.SECONDS);
        Mono<Student> st2 = mapCache.put(2, student2, 10, TimeUnit.SECONDS);

        StepVerifier.create(st1.then(st2).then())
                .verifyComplete();

        // access students
        mapCache.get(1).doOnNext(student -> log.info("{}", student)).subscribe();
        mapCache.get(2).doOnNext(student -> log.info("{}", student)).subscribe();

        super.sleep(3000);

        mapCache.get(1).doOnNext(student -> log.info("{}", student)).subscribe();
        mapCache.get(2).doOnNext(student -> log.info("{}", student)).subscribe();
    }

}
