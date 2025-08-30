package com.rperezv365.redisson.test;

import com.rperezv365.redisson.test.config.RedissonConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

/**
 * BaseTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 30/08/2025 - 18:32
 * @since 1.17
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    private final RedissonConfig redissonConfig = new RedissonConfig();

    protected RedissonReactiveClient client;

    @BeforeAll
    public void setClient() {
        this.client = this.redissonConfig.getReactiveClient();
    }

    @AfterAll
    public void shutdown() {
        if (this.client != null) {
            this.client.shutdown();
        }
    }

}
