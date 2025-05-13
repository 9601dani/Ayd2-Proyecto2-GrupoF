package com.codenbugs.ms_company;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class MsCompanyApplicationMainTest {

	@Test
	void testMainRuns() {
		try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
			MsCompanyApplication.main(new String[]{});
			mocked.verify(() -> SpringApplication.run(MsCompanyApplication.class, new String[]{}));
		}
	}

	@Test
	void testConstructor() {
		new MsCompanyApplication();
	}
}
