package org.example.configuration;


import org.example.component.TestComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TestProperties.class)
public class TestConfiguration {

    @Autowired
    TestProperties testProperties;

    @Bean
    @ConditionalOnMissingBean
    public TestComponent testComponent() {
        System.out.println(testProperties.getFeatureToggle());
        return new TestComponent(testProperties.getFeatureToggle());
    }

}
