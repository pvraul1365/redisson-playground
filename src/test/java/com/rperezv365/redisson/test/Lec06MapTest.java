package com.rperezv365.redisson.test;

import com.rperezv365.redisson.test.dto.Student;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Lec06MapTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 01/09/2025 - 19:38
 * @since 1.17
 */
public class Lec06MapTest extends BaseTest {

    @Test
    public void mapTest1() {

        RMapReactive<String, String> map = super.client.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "sam");
        Mono<String> age = map.put("age", "10");
        Mono<String> city = map.put("city", "atlanta");

        StepVerifier.create(name.concatWith(age).concatWith(city).then())
                .verifyComplete();
    }

    @Test
    public void mapTest2() {

        RMapReactive<String, String> map = super.client.getMap("user:2", StringCodec.INSTANCE);

        Map<String, String> javaMap = Map.of(
                "name", "jake",
                "age", "30",
                "city", "miami"
        );

        StepVerifier.create(map.putAll(javaMap).then())
                .verifyComplete();
    }

    @Test
    public void mapTest3() {
        // Map<Integer, Student>
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Integer, Student> map = super.client.getMap("users", codec);

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

        Mono<Student> mono1 = map.put(1, student1);
        Mono<Student> mono2 = map.put(2, student2);

        StepVerifier.create(mono1.concatWith(mono2).then())
                .verifyComplete();
    }


}
