package cn.zjut;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LatencyTest {

	private final static int testTimes = 10;
	@Autowired
	UserService userService;

	private static final List<String> SAMPLE_COUNTRY_CODES = Arrays.asList("AF", "AX", "AL", "DZ", "AS", "AD", "AO",
			"AI", "AQ", "AG");

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	@Order(1)
	public void testOfGetLatencyTest() {
		int retry = 0;
		try {
			while (!this.restTemplate.getForObject("http://localhost:" + port + "/",
					String.class).contains("Hello, World") && retry < 10) {
				Thread.sleep(200);
				retry++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Tomcat Initialized");
	}

	@RepeatedTest(testTimes)
	@Order(2)
	public void getLatencyTest() throws Exception {
		long time1 = System.currentTimeMillis();
		this.restTemplate.getForObject("http://localhost:" + port + "/",
				String.class);
		long time2 = System.currentTimeMillis();
		long latency = time2 - time1;
		System.out.println("get latency(ms): " + latency);
	}

	@Test
	@Order(3)
	public void testOfPostLatencyTest() {
		try {
			UUID uuid = UUID.randomUUID();
			long time1 = System.currentTimeMillis();
			this.restTemplate.postForEntity("http://localhost:" + port + "/sendMeASecret", uuid,
					UUID.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RepeatedTest(testTimes)
	@Order(4)
	public void postLatencyTest() throws Exception {
		UUID uuid = UUID.randomUUID();
		long time1 = System.currentTimeMillis();
		this.restTemplate.postForEntity("http://localhost:" + port + "/sendMeASecret", uuid,
				UUID.class);
		long time2 = System.currentTimeMillis();
		long latency = time2 - time1;
		System.out.println("post latency(ms): " + latency);
	}

	@Test
	@Order(5)
	public void initCache(){
		for(String country: SAMPLE_COUNTRY_CODES){
			long time1 = System.currentTimeMillis();
			User user = userService.getUserByCountry(country);
			long time2 = System.currentTimeMillis();
			long latency = time2 - time1;
			System.out.println("uncached latency(ms): " + latency);
		}
	}

	@Test
	@Order(6)
	public void cacheTest(){
		for(String country: SAMPLE_COUNTRY_CODES){
			long time1 = System.currentTimeMillis();
			User user = userService.getUserByCountry(country);
			long time2 = System.currentTimeMillis();
			long latency = time2 - time1;
			System.out.println("cached latency(ms): " + latency);
		}
	}
}
