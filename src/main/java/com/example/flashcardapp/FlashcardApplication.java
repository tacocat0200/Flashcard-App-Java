package com.example.flashcardapp;

import com.example.flashcardapp.core.Flashcard;
import com.example.flashcardapp.db.FlashcardDAO;
import com.example.flashcardapp.resources.FlashcardResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

public class FlashcardApplication extends Application<FlashcardAppConfiguration> {

    // 1. Hibernate Bundle for managing database interactions
    private final HibernateBundle<FlashcardAppConfiguration> hibernateBundle =
        new HibernateBundle<FlashcardAppConfiguration>(Flashcard.class) {
            @Override
            public DataSourceFactory getDataSourceFactory(FlashcardAppConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };

    // 2. Main method to launch the application
    public static void main(final String[] args) throws Exception {
        new FlashcardApplication().run(args);
    }

    // 3. Application name
    @Override
    public String getName() {
        return "flashcard-app";
    }

    // 4. Initialize method to add bundles and perform setup before the application runs
    @Override
    public void initialize(final Bootstrap<FlashcardAppConfiguration> bootstrap) {
        // Adding Hibernate bundle to manage ORM
        bootstrap.addBundle(hibernateBundle);

        // Adding Swagger/OpenAPI support for API documentation
        bootstrap.addBundle(new io.dropwizard.servlets.assets.AssetsBundle("/swagger-ui", "/swagger-ui", "index.html"));
    }

    // 5. Run method to configure the environment and register resources
    @Override
    public void run(final FlashcardAppConfiguration configuration,
                    final Environment environment) {
        // Setting up DAO (Data Access Object) with Hibernate session factory
        final FlashcardDAO flashcardDAO = new FlashcardDAO(hibernateBundle.getSessionFactory());

        // Registering RESTful resources with Jersey
        environment.jersey().register(new FlashcardResource(flashcardDAO));

        // Enabling role-based access control features
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        // Registering OpenAPI/Swagger resource for API documentation
        environment.jersey().register(new OpenApiResource());

        // Additional configurations (e.g., authentication filters) can be added here
    }

}
