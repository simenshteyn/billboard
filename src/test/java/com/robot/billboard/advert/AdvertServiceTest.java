package com.robot.billboard.advert;

import com.robot.billboard.user.User;
import com.robot.billboard.user.UserStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdvertServiceTest {
    @InjectMocks
    private AdvertService advertService;
    @Mock
    private AdvertStorage advertStorage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private MessageStorage messageStorage;

    @Test
    void getAdvertById() {
        Advert advert = new Advert();
        advert.setId(42L);
        when(advertStorage.getAdvertById(Mockito.anyLong())).thenReturn(Optional.of(advert));
        Advert result = advertService.getAdvertById(42L);
        assertEquals(42L, result.getId());
    }

    @Test
    void getAdvertsForUserById() {
        Advert advert = new Advert();
        advert.setName("test advert");
        List<Advert> advertList = Collections.singletonList(advert);
        when(advertStorage.getAdvertsByUserId(Mockito.anyLong())).thenReturn(advertList);
        List<Advert> result = advertService.getAdvertsForUserById(42L);
        assertEquals(1, result.size());
    }

    @Test
    void createAdvert() {
        Advert advert = new Advert();
        advert.setName("name");
        when(advertStorage.addAdvert(any(Advert.class))).thenReturn(Optional.of(advert));
        Advert result = advertService.createAdvert(advert);
        assertEquals(advert.getName(), result.getName());
    }

    @Test
    void updateAdvert() {
        Advert advert = new Advert();
        advert.setAvailable(true);
        when(advertStorage.updateAdvert(Mockito.anyLong(), any(Advert.class))).thenReturn(Optional.of(advert));
        Advert result = advertService.updateAdvert(42L, advert);
        assertEquals(advert.getAvailable(), result.getAvailable());
    }

    @Test
    void deleteAdvertById() {
        Advert advert = new Advert();
        advert.setPhone("12345");
        when(advertStorage.deleteAdvertById(Mockito.anyLong())).thenReturn(Optional.of(advert));
        Advert result = advertService.deleteAdvertById(42L);
        assertEquals(advert.getPhone(), result.getPhone());
    }

    @Test
    void createAdvertMessage() {
        Message message = new Message();
        message.setText("foo");
        User user = new User();
        user.setId(43L);
        user.setEmail("test@mail.ru");
        Advert advert = new Advert();
        advert.setDescription("desc foo");
        advert.setOwner(new User());
        when(userStorage.getUser(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(advertStorage.getAdvertById(Mockito.anyLong())).thenReturn(Optional.of(advert));
        when(messageStorage.addMessage(any(Message.class))).thenReturn(Optional.of(message));
        Message result = advertService.createAdvertMessage(42L, 41L, message);
        assertEquals(message.getText(), result.getText());
    }

    @Test
    void getAdvertMessagesById() {
        Message message = new Message();
        message.setText("foo");
        User user = new User();
        user.setEmail("foo@mail.ru");
        Advert advert = new Advert();
        advert.setName("foo name");
        when(userStorage.getUser(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(advertStorage.getAdvertById(Mockito.anyLong())).thenReturn(Optional.of(advert));
        when(messageStorage.getMessagesForAdvertByAuthor(any(Advert.class), any(User.class)))
                .thenReturn(Collections.singletonList(message));
        List<Message> result = advertService.getAdvertMessagesById(42L, 42L);
        assertEquals(1, result.size());
    }

    @Test
    void getRepliesForAdvert() {
        User user = new User();
        user.setRole("ROLE_USER");
        user.setId(42L);
        Advert advert = new Advert();
        advert.setName("foo advert");
        advert.setOwner(user);
        Message message = new Message();
        message.setText("foo");
        when(advertStorage.getAdvertById(Mockito.anyLong())).thenReturn(Optional.of(advert));
        when(userStorage.getUser(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(messageStorage.getMessagesForAdvertByAuthor(any(Advert.class), any(User.class)))
                .thenReturn(Collections.singletonList(message));
        List<Message> result = advertService.getRepliesForAdvert(42L, 42L);
        assertEquals(1, result.size());
    }

    @Test
    void getAllRepliesForAdvert() {
        Advert advert = new Advert();
        advert.setDescription("foo desc");
        Message message = new Message();
        message.setText("foo text");
        when(advertStorage.getAdvertById(Mockito.anyLong())).thenReturn(Optional.of(advert));
        when(messageStorage.getAllRepliesForAdvert(any(Advert.class))).thenReturn(Collections.singletonList(message));
        List<Message> result = advertService.getAllRepliesForAdvert(42L);
        assertEquals(1, result.size());
    }

    @Test
    void replyToAdvertMessage() {
        User user = new User();
        user.setRole("ROLE_USER");
        user.setId(42L);
        Advert advert = new Advert();
        advert.setDescription("foo desc");
        advert.setId(42L);
        advert.setOwner(user);
        Message message = new Message();
        message.setText("foo text");
        when(advertStorage.getAdvertById(Mockito.anyLong())).thenReturn(Optional.of(advert));
        when(messageStorage.addMessage(any(Message.class))).thenReturn(Optional.of(message));
        when(userStorage.getUser(Mockito.anyLong())).thenReturn(Optional.of(user));
        Message result = advertService.replyToAdvertMessage(42L, 42L, message);
        assertEquals(message.getText(), result.getText());
    }
}