package com.etornamklu.bookmgtsystem.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestWebConfig {

    @Bean
    @Primary
    public JpaMetamodelMappingContext jpaMetamodelMappingContext() {
        return mock(JpaMetamodelMappingContext.class);
    }
}
