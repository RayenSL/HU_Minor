package com.hu.assignment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AssignmentApplicationTests {

    @BeforeAll
    static void setup() {
        System.out.println("@BeforeAll");
    }


    @Test
    void contextLoads() {
    }

}
