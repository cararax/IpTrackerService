package com.carara;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class for testing the IP Tracker service
 */
@DisplayName("IpTrackerTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IpTrackerTest {
    private static final String BASE_URL = "https://qptmjqy871.execute-api.us-east-1.amazonaws.com/Prod";

    private List<String> ips;

    private HttpClient client;
    private Gson gson;

    @BeforeAll
    public void setup() {
//        ips = generateIps();

        client = HttpClient.newHttpClient();
        gson = new Gson();
    }

    /**
     * Test method for the request-handled endpoint with generated IPs
     *
     * @throws IOException          if there is an error making the HTTP request
     * @throws InterruptedException if the thread is interrupted while waiting for the response
     */
    @Test
    @Order(1)
    @DisplayName("Test request-handled endpoint with generated IPs")
    void testRequestHandleWithGeneratedIPs() throws IOException, InterruptedException {
        List<String> ips = generateIps();

        int numIPs = ips.size();
        int count = 0;

        long startTime = System.currentTimeMillis();
        for (String ip : ips) {
            String requestBody = String.format("{ \"ip\": \"%s\" }", ip);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/request-handled"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            count++;
            System.out.printf("Sent IP %d of %d\n", count, numIPs);
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        double avgTime = (double) totalTime / numIPs;
        System.out.printf("Sent %d IPs in %.2f s with average time of %.2f ms per IP\n",
                numIPs, totalTime / 1000.0, avgTime);
    }

    /**
     * Test method for the top100 endpoint with generated IPs
     *
     * @throws IOException          if there is an error making the HTTP request
     * @throws InterruptedException if the thread is interrupted while waiting for the response
     */
    @Test
    @Order(2)
    @DisplayName("Test top100 endpoint with generated IPs")
    void testTop100WithGeneratedIPs() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/top100"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        long startTime = System.currentTimeMillis();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        long endTime = System.currentTimeMillis();

        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(200, response.statusCode());
        assertEquals(99, jsonArray.size());
        System.out.printf("Request completed in %d ms\n", endTime - startTime);
    }

    /**
     * Test method for the clear endpoint
     *
     * @throws IOException          if there is an error making the HTTP request
     * @throws InterruptedException if the thread is interrupted while waiting for the response
     */
    @Test
    @Order(3)
    @DisplayName("Test clear endpoint")
    void testClear() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clear"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        //adaptado

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/top100"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
        assertTrue(jsonArray.isJsonNull() || jsonArray.size() == 0);
    }

    /**
     * Generates a list of 100 unique IP addresses, where the first 49 addresses
     * are "192.168.0.i", and each address is repeated i times, where i goes from 2 to 50.
     * The remaining 51 addresses are random, valid IP addresses that don't conflict with the first 49.
     *
     * @return A List of 100 unique IP addresses.
     */
    public List<String> generateIps() {
        List<String> ips = new ArrayList<>();

        for (int i = 50; i >= 2; i--) {
            String ip = "192.168.0." + i;
            for (int j = 0; j < i; j++) {
                ips.add(ip);
            }
        }

        for (int i = 0; i < 50; i++) {
            String ip;
            do {
                int a = (int) (Math.random() * 256);
                int b = (int) (Math.random() * 256);
                int c = (int) (Math.random() * 256);
                int d = (int) (Math.random() * 256);
                ip = a + "." + b + "." + c + "." + d;
            } while (ips.contains(ip));
            ips.add(ip);
        }

        return ips;
    }
}
