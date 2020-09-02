package com.shops.service.usr.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;


@Configuration
public class Config {
    /**
     *
    * */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)  // полное соотвецтвие
                .setFieldMatchingEnabled(true)  // одинаковые поля
                .setSkipNullEnabled(true)  // пустые пропускать
                .setFieldAccessLevel(PRIVATE);   // доступ к приватным полям
        return mapper;
    }
}
