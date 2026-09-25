package com.example.testapplication.utils;

import com.example.testapplication.TestResources;
import com.example.testapplication.model.WeatherResponse;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class WeatherFormatterTest {

    private final Gson gson = new Gson();

    @Test
    public void formatsFullResponse() throws Exception {
        WeatherResponse response = gson.fromJson(TestResources.read("weather_response.json"), WeatherResponse.class);

        String expected = "Pushcha-Voditsa\n"
                + "Broken clouds\n"
                + "Temperature: 12.3 °C (min 11.0, max 13.5)\n"
                + "Humidity: 66%\n"
                + "Pressure: 1030 hPa\n"
                + "Wind: 3.6 m/s, 10°\n"
                + "Clouds: 75%";
        assertEquals(expected, WeatherFormatter.format(response, Locale.US));
    }

    @Test
    public void usesLocaleDecimalSeparator() {
        WeatherResponse response = gson.fromJson("{\"wind\":{\"speed\":3.6,\"deg\":10}}", WeatherResponse.class);

        assertEquals("Wind: 3,6 m/s, 10°", WeatherFormatter.format(response, Locale.GERMANY));
    }

    @Test
    public void skipsMissingSections() {
        WeatherResponse response = gson.fromJson("{\"name\":\"Kyiv\",\"weather\":[]}", WeatherResponse.class);

        assertEquals("Kyiv", WeatherFormatter.format(response, Locale.US));
    }

    @Test
    public void returnsEmptyStringForEmptyResponse() {
        WeatherResponse response = gson.fromJson("{}", WeatherResponse.class);

        assertEquals("", WeatherFormatter.format(response, Locale.US));
    }
}
