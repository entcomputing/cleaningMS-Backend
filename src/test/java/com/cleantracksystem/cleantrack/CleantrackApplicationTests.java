package com.cleantracksystem.cleantrack;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CleantrackApplicationTests {

	@Value("${local.server.port}")
	private int port;

	@Test
	void contextLoads() {
	}

	@Test
	void healthEndpointIsAvailable() throws Exception {
		URL url = new URL("http://localhost:" + port + "/api/health");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		int status = con.getResponseCode();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			// consume body
			while (in.readLine() != null) {}
		}
		assertThat(status).isBetween(200, 299);
	}

}
