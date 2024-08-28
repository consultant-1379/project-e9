package com.team2.App;

import com.team2.DatabaseController;
import com.team2.SwaggerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {
    // Test class to verify all the RestControllers exist

    @Autowired
    private SwaggerController swaggerController;

    @Autowired
    private DatabaseController databaseController;

    @Test
    public void contextLoads() throws Exception{
        assertThat(swaggerController).isNotNull();
        assertThat(databaseController).isNotNull();
    }
}
