package github.andreepdias.fitnessmanager.config;

import github.andreepdias.fitnessmanager.service.DBService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevConfig {

    private final DBService dbService;

    @Bean
    public void instatiateDatabase(){
        dbService.instantiateDatabase();
    }
}
