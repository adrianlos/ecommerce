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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(SampleDataTestConfiguration.class)
public class AuthorControllerTest {

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
    public void listingAllAuthors() throws Exception {
        mockMvc.perform(get("/authors?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void gettingExistingAuthor() throws Exception {
        val author = fixture.janKowalski();

        mockMvc.perform(get("/authors/" + author.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.id", is(author.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(author.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(author.getLastName())));
    }

    @Test
    public void gettingNotExistingAuthor() throws Exception {
        mockMvc.perform(get("/products/" + 999_999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addingNewAuthor() throws Exception {
        AddAuthor request = new AddAuthor(fixture.janKowalski().getFirstName(), fixture.janKowalski().getLastName());
        String payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void deletingExistingAuthorWhenRelatedToSomeProducts() throws Exception {
        mockMvc.perform(delete("/authors/" + fixture.janKowalski().getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deletingExistingAuthorWhenRelatedToNoProducts() throws Exception {
        mockMvc.perform(delete("/authors/" + fixture.karolinaNowak().getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletingNonExistingAuthor() throws Exception {
        mockMvc.perform(delete("/authors/" + 999_999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
