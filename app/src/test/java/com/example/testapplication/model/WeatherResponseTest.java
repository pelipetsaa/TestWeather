package com.example.testapplication.model;

import com.example.testapplication.TestResources;
import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class WeatherResponseTest {

    private static final double DELTA = 1e-9;

    private final Gson gson = new Gson();

    @Test
    public void parsesFullResponse() throws Exception {
        WeatherResponse response = gson.fromJson(TestResources.read("weather_response.json"), WeatherResponse.class);

        assertEquals("Pushcha-Voditsa", response.getName());
        assertEquals(696050, response.getId());
        assertEquals(200, response.getCod());
        assertEquals(1443603600L, response.getDt());
        assertEquals("stations", response.getBase());

        assertEquals(30.5, response.getCoord().lon, DELTA);
        assertEquals(50.45, response.getCoord().lat, DELTA);

        assertEquals(1, response.getWeather().size());
        WeatherResponse.Weather weather = response.getFirstWeather();
        assertNotNull(weather);
        assertEquals(803, weather.id);
        assertEquals("Clouds", weather.main);
        assertEquals("broken clouds", weather.description);
        assertEquals("04d", weather.icon);

        assertEquals(12.34, response.getMain().temp, DELTA);
        assertEquals(11.0, response.getMain().temp_min, DELTA);
        assertEquals(13.5, response.getMain().temp_max, DELTA);
        assertEquals(1030, response.getMain().pressure);
        assertEquals(66, response.getMain().humidity);

        assertEquals(75, response.getClouds().all);
        assertEquals("UA", response.getSys().country);
        assertEquals(1443585388L, response.getSys().sunrise);
        assertEquals(1443627516L, response.getSys().sunset);
    }

    @Test
    public void parsesFractionalWindSpeed() {
        WeatherResponse response = gson.fromJson("{\"wind\":{\"speed\":3.6,\"deg\":10}}", WeatherResponse.class);

        assertEquals(3.6, response.getWind().speed, DELTA);
        assertEquals(10, response.getWind().deg);
    }

    @Test
    public void firstWeatherIsNullWhenWeatherMissing() {
        WeatherResponse response = gson.fromJson("{\"name\":\"Kyiv\"}", WeatherResponse.class);

        assertNull(response.getWeather());
        assertNull(response.getFirstWeather());
    }

    @Test
    public void firstWeatherIsNullWhenWeatherEmpty() {
        WeatherResponse response = gson.fromJson("{\"weather\":[]}", WeatherResponse.class);

        assertNull(response.getFirstWeather());
    }
}
