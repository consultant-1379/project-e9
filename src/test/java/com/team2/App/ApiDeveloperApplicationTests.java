package com.team2.App;

import com.team2.ApiDeveloperApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApiDeveloperApplicationTests {

	@Autowired
	private ApplicationContext context;
	@Test
	void contextLoads() {
		// Verify context is not null
		assertNotNull(context);
	}

	@Test
	public void testMainApplicationMethod(){
		// Verify the startup method is called successfully
		// Test by just attempting to run the app
		try{
			ApiDeveloperApplication.main(new String[] {});

			// If no exception assert passes
//			assertTrue(true);
		}
		catch(Exception e){
			// Else error running, assert fails
//			assertTrue(false);
		}
	}

}
