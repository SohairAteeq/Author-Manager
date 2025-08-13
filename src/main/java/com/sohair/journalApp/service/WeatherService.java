package com.sohair.journalApp.service;

import com.sohair.journalApp.api.response.WeatherResponse;
import com.sohair.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Autowired
    AppCache appCache;

    @Autowired
    RestTemplate restTemplate;

    @Value("${my.weather.api.key}")
    private String API_KEY;

    private String BASE_URL;

    public WeatherResponse getWeather(String city) {
        BASE_URL = appCache.APPCACHE.get("WEATHER_URL").replace("API_KEY", API_KEY).replace("CITY", city);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse weatherResponse = response.getBody();
        return weatherResponse;
    }
}
