package com.rperezv365.redisson.test;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
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


}
