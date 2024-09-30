package com.example.flashcardapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

public class FlashcardAppConfiguration extends Configuration {

    // 1. Database Configuration
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    // 2. JWT Configuration (If Implemented)
    @Valid
    @NotNull
    private JwtConfiguration jwt = new JwtConfiguration();

    @JsonProperty("jwt")
    public JwtConfiguration getJwtConfiguration() {
        return jwt;
    }

    @JsonProperty("jwt")
    public void setJwtConfiguration(JwtConfiguration jwt) {
        this.jwt = jwt;
    }

    // 3. Swagger Configuration (Optional)
    @Valid
    @NotNull
    private SwaggerConfiguration swagger = new SwaggerConfiguration();

    @JsonProperty("swagger")
    public SwaggerConfiguration getSwaggerConfiguration() {
        return swagger;
    }

    @JsonProperty("swagger")
    public void setSwaggerConfiguration(SwaggerConfiguration swagger) {
        this.swagger = swagger;
    }

    // 4. Additional Custom Configurations (If Any)
    // Add other configuration sections here as needed
}
