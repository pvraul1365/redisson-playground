package com.rperezv365.redisson.test.assignment;

import lombok.*;

/**
 * UserOrder
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 07/09/2025 - 11:59
 * @since 1.17
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrder {

    private int id;
    private Category category;

}
