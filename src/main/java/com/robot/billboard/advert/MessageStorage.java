package com.robot.billboard.advert;

import com.robot.billboard.user.User;

import java.util.List;
import java.util.Optional;

public interface MessageStorage {
    Optional<Message> addMessage(Message message);

    List<Message> getMessagesForAdvertByAuthor(Advert advert, User author);

    List<Message> getAllRepliesForAdvert(Advert advert);
}
