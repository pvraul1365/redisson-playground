package com.rperezv365.redisson.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RTopicReactive;
import org.redisson.client.codec.StringCodec;

/**
 * Lec12PubSubTest
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 06/09/2025 - 09:14
 * @since 1.17
 */
@Slf4j
public class Lec12PubSubTest extends BaseTest {

    @Test
    public void suscriber1() {
        RTopicReactive topic = super.client.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(e -> log.error("Error receiving message", e))
                .doOnNext(m -> log.info("Message received (1): {}", m))
                .subscribe();

        sleep(600_000);
    }

    @Test
    public void suscriber2() {
        RTopicReactive topic = super.client.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(e -> log.error("Error receiving message", e))
                .doOnNext(m -> log.info("Message received (2): {}", m))
                .subscribe();

        sleep(600_000);
    }

}
