package com.example.testapplication.utils;

import com.example.testapplication.model.WeatherResponse;

import java.util.Locale;

/**
 * Builds a human readable description of {@link WeatherResponse} received with metric units.
 */
public final class WeatherFormatter {

    private WeatherFormatter() {
    }

    public static String format(WeatherResponse response, Locale locale) {
        StringBuilder sb = new StringBuilder();
        if (response.getName() != null && !response.getName().isEmpty()) {
            sb.append(response.getName()).append('\n');
        }

        WeatherResponse.Weather weather = response.getFirstWeather();
        if (weather != null && weather.description != null) {
            sb.append(capitalize(weather.description, locale)).append('\n');
        }

        WeatherResponse.Main main = response.getMain();
        if (main != null) {
            sb.append(String.format(locale, "Temperature: %.1f °C (min %.1f, max %.1f)\n",
                    main.temp, main.temp_min, main.temp_max));
            sb.append(String.format(locale, "Humidity: %d%%\n", main.humidity));
            sb.append(String.format(locale, "Pressure: %d hPa\n", main.pressure));
        }

        WeatherResponse.Wind wind = response.getWind();
        if (wind != null) {
            sb.append(String.format(locale, "Wind: %.1f m/s, %d°\n", wind.speed, wind.deg));
        }

        WeatherResponse.Clouds clouds = response.getClouds();
        if (clouds != null) {
            sb.append(String.format(locale, "Clouds: %d%%\n", clouds.all));
        }

        return sb.toString().trim();
    }

    private static String capitalize(String text, Locale locale) {
        if (text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase(locale) + text.substring(1);
    }
}
