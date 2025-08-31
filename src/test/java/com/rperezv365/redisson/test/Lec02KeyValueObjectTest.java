package com.rperezv365.redisson.test;

import com.rperezv365.redisson.test.dto.Student;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec02BucketObjectTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 31/08/2025 - 10:42
 * @since 1.17
 */
@Slf4j
public class Lec02KeyValueObjectTest extends BaseTest {

    @Test
    public void keyValueObjectTest() {

        Student student = Student.builder()
                .name("marshal")
                .age(10)
                .city("Atlanta")
                .marks(Arrays.asList(1, 2, 3))
                .build();

        RBucketReactive<Student> bucket = super.client.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get()
                .doOnNext(mystudent -> log.info("student: {}", mystudent))
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

    }

}
