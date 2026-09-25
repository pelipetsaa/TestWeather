package com.example.testapplication.net;

import com.example.testapplication.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface IRestClient {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(@Query("lat") double lat, @Query("lon") double lon,
                                     @Query("units") String units, @Query("lang") String lang);
}
