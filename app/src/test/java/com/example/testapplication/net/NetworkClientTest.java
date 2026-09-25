package com.example.testapplication.net;

import com.example.testapplication.TestResources;
import com.example.testapplication.model.WeatherResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NetworkClientTest {

    private static final String API_KEY = "test-key";

    private MockWebServer server;
    private IRestClient api;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        api = new NetworkClient(server.url("/").toString(), API_KEY).getRestApi();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void sendsWeatherRequestWithApiKeyAndParameters() throws Exception {
        server.enqueue(new MockResponse().setBody(TestResources.read("weather_response.json")));

        api.getWeather(50.45, 30.5, "metric", "ru").execute();

        RecordedRequest request = server.takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        HttpUrl url = request.getRequestUrl();
        assertNotNull(url);
        assertEquals("/data/2.5/weather", url.encodedPath());
        assertEquals("50.45", url.queryParameter("lat"));
        assertEquals("30.5", url.queryParameter("lon"));
        assertEquals("metric", url.queryParameter("units"));
        assertEquals("ru", url.queryParameter("lang"));
        assertEquals(API_KEY, url.queryParameter("appid"));
    }

    @Test
    public void parsesSuccessfulResponse() throws Exception {
        server.enqueue(new MockResponse().setBody(TestResources.read("weather_response.json")));

        Response<WeatherResponse> response = api.getWeather(50.45, 30.5, "metric", "en").execute();

        assertTrue(response.isSuccessful());
        WeatherResponse body = response.body();
        assertNotNull(body);
        assertEquals("Pushcha-Voditsa", body.getName());
        assertEquals("04d", body.getFirstWeather().icon);
        assertEquals(3.6, body.getWind().speed, 1e-9);
    }

    @Test
    public void reportsUnauthorizedResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(401)
                .setBody("{\"cod\":401,\"message\":\"Invalid API key.\"}"));

        Response<WeatherResponse> response = api.getWeather(50.45, 30.5, "metric", "en").execute();

        assertFalse(response.isSuccessful());
        assertEquals(401, response.code());
        assertNull(response.body());
    }
}
