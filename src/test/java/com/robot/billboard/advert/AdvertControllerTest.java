package com.robot.billboard.advert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.robot.billboard.security.PersonDetails;
import com.robot.billboard.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class AdvertControllerTest {
    @MockBean
    AdvertService advertService;

    final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    private WebApplicationContext context;
    //@Autowireded
    private MockMvc mockMvc;

    @Mock
    PersonDetails principal;

    @BeforeEach
    public void beforeEach() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(securityContext);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    void getAdvertById() throws Exception {
        Advert advert = new Advert();
        advert.setId(42L);
        when(advertService.getAdvertById(Mockito.anyLong())).thenReturn(advert);
        mockMvc.perform(get("/adverts/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("42"));
    }

    @Test
    void getMyAdverts() throws Exception {
        Advert advert = new Advert();
        advert.setId(42L);
        User user = new User();
        user.setId(42L);
        when(principal.getUser()).thenReturn(user);
        when(advertService.getAdvertsForUserById(Mockito.anyLong())).thenReturn(Collections.singletonList(advert));
        mockMvc.perform(get("/adverts/my"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void createAdvert() throws Exception {
        User user = new User();
        user.setId(42L);
        Advert advert = new Advert();
        advert.setPhone("123");
        advert.setName("foo name");
        advert.setDescription("foo desc");
        advert.setAvailable(true);
        advert.setId(42L);
        advert.setOwner(user);
        when(principal.getUser()).thenReturn(user);
        when(advertService.createAdvert(any(Advert.class))).thenReturn(advert);
        mockMvc.perform(post("/adverts")
                .content(mapper.writeValueAsString(advert))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("42"));
    }

    @Test
    void updateAdvert() throws Exception {
        User user = new User();
        user.setId(42L);
        Advert advert = new Advert();
        advert.setAvailable(true);
        advert.setOwner(user);
        advert.setId(42L);
        when(principal.getUser()).thenReturn(user);
        when(advertService.getAdvertById(Mockito.anyLong())).thenReturn(advert);
        when(advertService.updateAdvert(Mockito.anyLong(), any(Advert.class))).thenReturn(advert);
        mockMvc.perform(patch("/adverts/42")
                .content(mapper.writeValueAsString(advert))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("42"));
    }

    @Test
    void deleteAdvertById() throws Exception {
        User user = new User();
        user.setId(42L);
        Advert advert = new Advert();
        advert.setName("foo");
        advert.setOwner(user);
        when(advertService.getAdvertById(Mockito.anyLong())).thenReturn(advert);
        when(principal.getUser()).thenReturn(user);
        when(advertService.deleteAdvertById(Mockito.anyLong())).thenReturn(advert);
        mockMvc.perform(delete("/adverts/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("foo"));
    }

    @Test
    void messageAdvertById() throws Exception {
        User sender = new User();
        sender.setId(42L);
        User user = new User();
        user.setId(21L);
        Advert advert = new Advert();
        advert.setOwner(user);
        Message message = new Message();
        message.setText("foo");
        when(advertService.getAdvertById(Mockito.anyLong())).thenReturn(advert);
        when(principal.getUser()).thenReturn(sender);
        when(advertService.createAdvertMessage(Mockito.anyLong(), Mockito.anyLong(), any(Message.class)))
                .thenReturn(message);
        mockMvc.perform(post("/adverts/42/messages")
                .content(mapper.writeValueAsString(message))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.text").value("foo"));
    }

    @Test
    void getAdvertMessagesById() throws Exception {
        User user = new User();
        user.setId(13L);
        User owner = new User();
        owner.setId(42L);
        Advert advert = new Advert();
        advert.setOwner(owner);
        Message message = new Message();
        message.setText("foo");
        when(advertService.getAdvertById(Mockito.anyLong())).thenReturn(advert);
        when(principal.getUser()).thenReturn(user);
        when(advertService.getAdvertMessagesById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Collections.singletonList(message));
        mockMvc.perform(get("/adverts/42/messages"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getMessagesAndRepliesForAdvert() throws Exception {
        User user = new User();
        user.setId(42L);
        User owner = new User();
        owner.setId(42L);
        Advert advert = new Advert();
        advert.setOwner(owner);
        Message message = new Message();
        message.setText("foo");
        when(advertService.getAdvertById(Mockito.anyLong())).thenReturn(advert);
        when(principal.getUser()).thenReturn(user);
        when(advertService.getRepliesForAdvert(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Collections.singletonList(message));
        mockMvc.perform(get("/adverts/42/reply/42"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAllRepliesForAdvert() throws Exception {
        User user = new User();
        user.setId(42L);
        User owner = new User();
        owner.setId(42L);
        Advert advert = new Advert();
        advert.setOwner(owner);
        Message message = new Message();
        message.setText("foo");
        when(advertService.getAdvertById(Mockito.anyLong())).thenReturn(advert);
        when(principal.getUser()).thenReturn(user);
        when(advertService.getAllRepliesForAdvert(Mockito.anyLong()))
                .thenReturn(Collections.singletonList(message));
        mockMvc.perform(get("/adverts/42/reply/all"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void replyToAdvertMessage() throws Exception {
        User user = new User();
        user.setId(42L);
        User owner = new User();
        owner.setId(42L);
        Advert advert = new Advert();
        advert.setOwner(owner);
        Message message = new Message();
        message.setText("foo reply");
        when(advertService.getAdvertById(Mockito.anyLong())).thenReturn(advert);
        when(principal.getUser()).thenReturn(user);
        when(advertService.replyToAdvertMessage(Mockito.anyLong(), Mockito.anyLong(), any(Message.class)))
                .thenReturn(message);
        mockMvc.perform(post("/adverts/42/reply/21")
                        .content(mapper.writeValueAsString(message))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.text").value("foo reply"));
    }
}