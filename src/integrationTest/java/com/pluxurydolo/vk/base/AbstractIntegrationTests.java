package com.pluxurydolo.vk.base;

import com.pluxurydolo.vk.TestApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(classes = TestApplication.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTests {
}
