package com.example.testapplication.net;

import com.example.testapplication.model.WeatherResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface IRestClient {
    @GET("/data/2.5/weather")
    public void getWeather(@Query("lat") double lat, @Query("lon") double lon, Callback<WeatherResponse> weatherCallback);
}