package org.springframework.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;

@WebMvcTest(TestController.class)
public class ControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testIndex() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}

	@Test
	public void testForward() throws Exception {
		assertThat(
				mockMvc.perform(get("/test")).andExpect(status().isOk()).andReturn().getResponse()
						.getForwardedUrl())
				.isEqualTo("index");
	}
}

@SpringBootApplication
class TestApplication {
}

@Controller
class TestController {
	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/test")
	public String test() {
		return "forward:index";
	}
}
