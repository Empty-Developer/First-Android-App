// Правильный путь: app/src/main/java/com/example/greeanstatus/api/services/WeatherApiService.java
package com.example.greeanstatus.api.services;

import com.example.greeanstatus.api.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("weather")
    Call<WeatherResponse> getCurrentWeather(
            @Query("q") String city,
            @Query("units") String units,
            @Query("appid") String apiKey,
            @Query("lang") String language
    );
}