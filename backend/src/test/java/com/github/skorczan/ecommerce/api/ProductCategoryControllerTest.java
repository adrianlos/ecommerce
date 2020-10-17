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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(SampleDataTestConfiguration.class)
public class ProductCategoryControllerTest {

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
    public void listingAllProductCategories() throws Exception {
        mockMvc.perform(get("/products/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(8)));
    }

    @Test
    public void gettingExistingProductCategory() throws Exception {
        val productCategory = fixture.manClothes();

        mockMvc.perform(get("/products/categories/" + productCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.id", is(productCategory.getId().intValue())))
                .andExpect(jsonPath("$.name", is(productCategory.getName())))
                .andExpect(jsonPath("$.parentCategoryId", is(productCategory.getParentCategory().getId().intValue())));
    }

    @Test
    public void gettingNotExistingAuthor() throws Exception {
        mockMvc.perform(get("/products/categories/" + 999_999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addingNewRootProductCategory() throws Exception {
        CreateProductCategoryRequest request = new CreateProductCategoryRequest("Food", null);
        String payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/products/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void addingNewProductSubCategory() throws Exception {
        CreateProductCategoryRequest request = new CreateProductCategoryRequest("Newborn's clothes", fixture.childClothes().getId());
        String payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/products/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void updatingExistingProductCategory() throws Exception {
        CreateProductCategoryRequest request = new CreateProductCategoryRequest("Renamed", null);
        String payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/products/categories/" + fixture.childClothes().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.id", is(fixture.childClothes().getId().intValue())))
                .andExpect(jsonPath("$.name", is("Renamed")))
                .andExpect(jsonPath("$.parentCategoryId", is(nullValue())));
    }

    @Test
    public void deletingOnlyOneProductCategory() throws Exception {
        mockMvc.perform(delete("/products/categories/" + fixture.drinks().getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/products/categories/" + fixture.fizzyDrinks().getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parentCategoryId", is(nullValue())));

        mockMvc.perform(get("/products/categories/" + fixture.stillDrinks().getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parentCategoryId", is(nullValue())));
    }

    @Test
    public void deletingProductCategoryAndWholeSubtree() throws Exception {
        mockMvc.perform(delete("/products/categories/" + fixture.drinks().getId() + "?wholeSubtree=true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/products/categories/" + fixture.fizzyDrinks().getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/products/categories/" + fixture.stillDrinks().getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletingNonExistingProductCategory() throws Exception {
        mockMvc.perform(delete("/products/categories/" + 999_999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
