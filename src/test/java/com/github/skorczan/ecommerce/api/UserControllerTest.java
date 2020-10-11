package com.github.skorczan.ecommerce.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.skorczan.ecommerce.SampleDataTestConfiguration;
import com.github.skorczan.ecommerce.application.AddressDto;
import com.github.skorczan.ecommerce.configuration.SampleDataFixture;
import com.github.skorczan.ecommerce.domain.User;
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
public class UserControllerTest {

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
    public void listingAllUsers() throws Exception {
        mockMvc.perform(get("/users?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void gettingExistingUser() throws Exception {
        val customer = fixture.customer();

        mockMvc.perform(get("/users/" + customer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(6)))
                .andExpect(jsonPath("$.id", is(customer.getId().intValue())))
                .andExpect(jsonPath("$.login", is(customer.getLogin())))
                .andExpect(jsonPath("$.role", is(customer.getRole().name())));
    }

    @Test
    public void gettingNotExistingAuthor() throws Exception {
        mockMvc.perform(get("/users/" + 999_999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void registeringACustomer() throws Exception {
        UserRegistrationRequest request = registrationOf(fixture.customer());
        String payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
//    TODO: Enable when unregistering users is supported
//    @Test
//    public void deletingExistingCustomer() throws Exception {
//        mockMvc.perform(delete("/users/" + fixture.customer().getId())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void deletingNonExistingCustomer() throws Exception {
//        mockMvc.perform(delete("/users/" + 999_999)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//    }

    private UserRegistrationRequest registrationOf(User user) {
        return UserRegistrationRequest.builder()
                .login("new" + user.getLogin())
                .password("sia1aBabaM@k!")
                .role(UserRegistrationRequest.Role.valueOf(user.getRole().name()))
                .contactPreference(UserRegistrationRequest.ContactPreference.valueOf(user.getContactPreference().name()))
                .avatarUrl(user.getAvatarUrl())
                .address(new AddressDto(
                        user.getAddress().getCountry(),
                        user.getAddress().getCity(),
                        user.getAddress().getStreet(),
                        user.getAddress().getZipCode()))
                .build();
    }
}
