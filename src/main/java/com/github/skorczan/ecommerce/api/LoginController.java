package com.github.skorczan.ecommerce.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class LoginController { // needed by Swagger only; /login endpoint provided by Spring Security

    @PostMapping("/login")
    void login(@RequestBody UserCredentials userCredentials) {
    }
}
