package com.sagnik.Ecom.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	/** Creates the mapper used to convert between entities and DTOs. */
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
