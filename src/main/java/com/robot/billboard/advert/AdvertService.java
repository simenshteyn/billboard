package com.robot.billboard.advert;

import com.robot.billboard.user.User;
import com.robot.billboard.user.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvertService {
    private final AdvertStorage advertStorage;
    private final UserStorage userStorage;
    private final MessageStorage messageStorage;

    @Autowired
    public AdvertService(AdvertStorage advertStorage, UserStorage userStorage, MessageStorage messageStorage) {
        this.advertStorage = advertStorage;
        this.userStorage = userStorage;
        this.messageStorage = messageStorage;
    }

    public List<Advert> getAllAdverts(int from, int size, boolean available) {
        return advertStorage.getAllAdverts(from, size, available);
    }

    public Advert getAdvertById(Long advertId) {
        return advertStorage.getAdvertById(advertId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find advert")
        );
    }

    public List<Advert> getAdvertsForUserById(Long userId) {
        return advertStorage.getAdvertsByUserId(userId);
    }

    public Advert createAdvert(Advert advert) {
        return advertStorage.addAdvert(advert).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add advert")
        );
    }

    public Advert updateAdvert(Long advertId, Advert advert) {
        return advertStorage.updateAdvert(advertId, advert).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to fine advert")
        );
    }

    public Advert deleteAdvertById(Long advertId) {
        return advertStorage.deleteAdvertById(advertId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find advert")
        );
    }

    public Message createAdvertMessage(Long advertId, Long userId, Message message) {
        User user = userStorage.getUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Advert advert = advertStorage.getAdvertById(advertId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find advert")
        );
        message.setAuthorId(user.getId());
        message.setAdvertId(advert.getId());
        message.setRecipientId(advert.getOwner().getId());
        message.setCreated(LocalDateTime.now());
        return messageStorage.addMessage(message).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add message")
        );
    }

    public List<Message> getAdvertMessagesById(Long advertId, Long userId) {
        User author = userStorage.getUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find author")
        );
        Advert advert = advertStorage.getAdvertById(advertId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find advert")
        );
        return messageStorage.getMessagesForAdvertByAuthor(advert, author);
    }

    public List<Message> getRepliesForAdvert(Long advertId, Long recipientId) {
        Advert advert = advertStorage.getAdvertById(advertId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find advert")
        );
        User author = userStorage.getUser(advert.getOwner().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find author")
        );
        User recipient = userStorage.getUser(recipientId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find recipient")
        );
        return messageStorage.getMessagesForAdvertByAuthor(advert, recipient);
    }

    public List<Message> getAllRepliesForAdvert(Long advertId) {
        Advert advert = advertStorage.getAdvertById(advertId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find advert")
        );
        return messageStorage.getAllRepliesForAdvert(advert);
    }

    public Message replyToAdvertMessage(Long advertId, Long recipientId, Message message) {
        Advert advert = advertStorage.getAdvertById(advertId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find advert")
        );
        User author = userStorage.getUser(advert.getOwner().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find author")
        );
        User recipient = userStorage.getUser(recipientId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find recipient")
        );
        message.setAdvertId(advert.getId());
        message.setAuthorId(author.getId());
        message.setRecipientId(recipient.getId());
        message.setCreated(LocalDateTime.now());
        return messageStorage.addMessage(message).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add message")
        );
    }

}
