package com.sohair.journalApp.api.response;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherResponse {
    private Current current;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Current{
        public int temperature;
        public ArrayList<String> weather_descriptions;
    }
}
