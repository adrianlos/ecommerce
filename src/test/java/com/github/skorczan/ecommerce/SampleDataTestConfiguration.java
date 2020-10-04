package com.github.skorczan.ecommerce;

import com.github.skorczan.ecommerce.configuration.SampleDataFixture;
import com.github.skorczan.ecommerce.domain.AuthorRepository;
import com.github.skorczan.ecommerce.domain.ProductCategoryRepository;
import com.github.skorczan.ecommerce.domain.ProductRepository;
import com.github.skorczan.ecommerce.domain.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

@TestConfiguration
public class SampleDataTestConfiguration {

    @Bean
    public SampleDataFixture sampleDataFixture(AuthorRepository authorRepository,
                                               ProductCategoryRepository productCategoryRepository,
                                               ProductRepository productRepository,
                                               UserRepository userRepository,
                                               EntityManager entityManager) {
        return new SampleDataFixture(authorRepository, productCategoryRepository, productRepository, userRepository, entityManager);
    }
}
