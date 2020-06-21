package com.flexicore.test;

import com.flexicore.model.Baseclass;
import com.flexicore.provider.EntitiesHolder;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import javax.persistence.Entity;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class EntitiesProviderTest {

    private static final Logger logger= LoggerFactory.getLogger(com.flexicore.provider.EntitiesProvider.class);

    /**
     * this will return all entities in flexicore and in ${flexicore.entities} path
     * we make sure to limit the search so this wont cause the loading of unwanted classes with that loader
     * in fact if we did do that several app critical classes(direct FC dependencies) will be loaded by the Reflection library
     * causing ClassNotFound exceptions and making meta model classes fields types to be null
     * @return
     */

    @Primary
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public EntitiesHolder getEntitiesTest(EntityProviderClasses entityProviderClasses) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ConfigurationBuilder configuration = ConfigurationBuilder.build()
                .addClassLoader(classLoader)
                .addUrls(entityProviderClasses.getSeedClasses().stream().map(f->f.getProtectionDomain().getCodeSource().getLocation()).collect(Collectors.toSet()));
        Reflections reflections = new Reflections(configuration);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Entity.class);
        return new EntitiesHolder(typesAnnotatedWith);
    }

    private URL getFCLocation() {
        return Baseclass.class.getProtectionDomain().getCodeSource().getLocation();

    }


}
