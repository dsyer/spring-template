package org.springframework.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = SampleController.class, properties = "spring.test.mockmvc.print=NONE")
public class SampleControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testIndex() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}

}