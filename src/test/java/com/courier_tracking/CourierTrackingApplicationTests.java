package com.courier_tracking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CourierTrackingApplicationTests {

	@Test
	void contextLoads() {
		// Verifies that the application context loads successfully
	}

	@Test
	void applicationStartsSuccessfully() {
		// Verifies that the application can start without errors
	}

	@Test
	void beansAreCreated() {
		// Verifies that all required beans are created
	}
}
