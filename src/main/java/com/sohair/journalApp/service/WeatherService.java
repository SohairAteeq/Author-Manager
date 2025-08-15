package com.sohair.journalApp.service;

import com.sohair.journalApp.api.response.WeatherResponse;
import com.sohair.journalApp.cache.AppCache;
import com.sohair.journalApp.containers.WeatherContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Autowired
    RedisService redisService;

    @Autowired
    AppCache appCache;

    @Autowired
    RestTemplate restTemplate;

    @Value("${my.weather.api.key}")
    private String API_KEY;

    private String BASE_URL;

    public WeatherResponse getWeather(String city) {
        BASE_URL = appCache.APPCACHE.get(AppCache.keys.WEATHER_URL.toString()).replace(WeatherContainer.API_KEY, API_KEY).replace(WeatherContainer.CITY, city);
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null) {
            return weatherResponse;
        }
        else{
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, null, WeatherResponse.class);
            weatherResponse = response.getBody();
            redisService.set("weather_of_" + city, weatherResponse, 100);
            return weatherResponse;
        }
    }
}
