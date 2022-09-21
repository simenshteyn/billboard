package com.robot.billboard.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.robot.billboard.security.JWTToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @MockBean
    UserService userService;

    final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    private MockMvc mockMvc;


    @WithAnonymousUser
    @Test
    void signupUser() throws Exception {
        User request = new User();
        request.setEmail("test@email.ru");
        request.setPassword("password some");
        request.setRole("ROLE_USER");
        JWTToken result = new JWTToken(request.getEmail(), "valid_token");
        when(userService.signupUser(any(User.class))).thenReturn(result);
        mockMvc.perform(post("/users/signup")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("valid_token"));
    }

    @WithAnonymousUser
    @Test
    void loginUser() throws Exception {
        User request = new User();
        request.setEmail("test@email.ru");
        request.setPassword("some password");
        JWTToken result = new JWTToken(request.getEmail(), "valid_token");
        when(userService.loginUser(any(User.class))).thenReturn(result);
        mockMvc.perform(post("/users/login")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("valid_token"));
    }
}