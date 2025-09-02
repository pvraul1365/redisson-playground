package com.rperezv365.redisson.test;

import com.rperezv365.redisson.test.config.RedissonConfig;
import com.rperezv365.redisson.test.dto.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;

/**
 * Lec08LocalCachedMapTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 02/09/2025 - 18:19
 * @since 1.17
 */
public class Lec08LocalCachedMapTest extends BaseTest {

    private RLocalCachedMap<Integer, Student> studentsMap;

    @BeforeAll
    public void setupClient() {
        RedissonConfig config = new RedissonConfig();
        RedissonClient redissonClient = config.getClient();

        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.<Integer, Student>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE);

        this.studentsMap = redissonClient.getLocalCachedMap(
            "students",
                new TypedJsonJacksonCodec(Integer.class, Student.class),
                mapOptions
        );
    }

    @Test
    public void localCachedMapTest() {

    }

}
