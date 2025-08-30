package com.rperezv365.redisson.test.config;

import java.util.Objects;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

/**
 * RedissonConfig
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 30/08/2025 - 18:26
 * @since 1.17
 */
public class RedissonConfig {

    private RedissonClient redissonClient;

    public RedissonClient getClient() {
        if (Objects.isNull(this.redissonClient)) {
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("redis://127.0.0.1:6379");
            this.redissonClient = Redisson.create(config);
        }

        return this.redissonClient;
    }

    public RedissonReactiveClient getReactiveClient() {
        return this.getClient().reactive();
    }

}
