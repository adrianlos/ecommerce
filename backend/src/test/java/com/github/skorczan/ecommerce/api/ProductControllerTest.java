package com.github.skorczan.ecommerce.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.skorczan.ecommerce.SampleDataTestConfiguration;
import com.github.skorczan.ecommerce.configuration.SampleDataFixture;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(SampleDataTestConfiguration.class)
public class ProductControllerTest {

    private static final Pageable ALL_PRODUCTS_PAGE = PageRequest.of(0, 9999);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SampleDataFixture fixture;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        fixture.save();
    }

    @AfterEach
    public void tearDown() {
        fixture.remove();
    }

    @Test
    public void listingAllProducts() throws Exception {
        mockMvc.perform(get("/products?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void gettingExistingProduct() throws Exception {
        val product = fixture.buty();

        mockMvc.perform(get("/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(8)))
                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                .andExpect(jsonPath("$.title", is(product.getTitle())))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.thumbnailUrl", is(product.getThumbnailUrl())))
                .andExpect(jsonPath("$.price", is(product.getPrice())))
                .andExpect(jsonPath("$.type", is(product.getType().name())))
                .andExpect(jsonPath("$.category.id", is(product.getCategory().getId().intValue())))
                .andExpect(jsonPath("$.author.id", is(product.getAuthor().getId().intValue())));
    }

    @Test
    public void gettingNotExistingProduct() throws Exception {
        mockMvc.perform(get("/products/" + 999_999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addingNewProduct() throws Exception {
        CreateProductRequest request = CreateProductRequest.builder()
                .title(fixture.szmata().getTitle())
                .description(fixture.szmata().getDescription())
                .thumbnailUrl(fixture.szmata().getThumbnailUrl())
                .price(fixture.szmata().getPrice())
                .type(fixture.szmata().getType().name())
                .categoryId(fixture.szmata().getCategory().getId())
                .authorId(fixture.szmata().getAuthor().getId())
                .build();

        String payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}
