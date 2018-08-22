package com.capgemini;

import com.capgemini.service.impl.ServiceTestSuite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(Suite.class)
@Suite.SuiteClasses({ServiceTestSuite.class})
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

}
