package com.github.skorczan.ecommerce.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Profile("sample-data")
@Service
@Transactional
@RequiredArgsConstructor
public class SampleDataSeeder {

    private final SampleDataFixture sampleDataFixture;

    @PostConstruct
    public void seed() {
        if (sampleDataFixture.shouldBeSaved()) {
            sampleDataFixture.save();
        }
    }
}
