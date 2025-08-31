package com.rperezv365.redisson.test.dto;

import java.util.List;
import lombok.*;

/**
 * Student
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 31/08/2025 - 10:44
 * @since 1.17
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    private String name;
    private int age;
    private String city;
    private List<Integer> marks;

}
