package com.sohair.journalApp.cache;

import com.sohair.journalApp.model.JournalConfigEntity;
import com.sohair.journalApp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class AppCache {

    public enum keys {
        WEATHER_URL
    }

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    public Map<String, String> APPCACHE;

    @PostConstruct
    public void init(){
        APPCACHE = new HashMap<>();
        List<JournalConfigEntity> all = configJournalAppRepository.findAll();
        all.forEach(e -> {
            APPCACHE.put(e.getKey(), e.getValue());
        });
    }
}
