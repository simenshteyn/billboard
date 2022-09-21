package com.robot.billboard.advert;

import com.robot.billboard.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersistentMessageStorage implements MessageStorage {
    private final MessageRepository messageRepository;

    @Autowired
    public PersistentMessageStorage(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Optional<Message> addMessage(Message message) {
        System.out.println("TIME MESSAGE IS: " + message.getCreated());
        return Optional.of(messageRepository.save(message));
    }

    @Override
    public List<Message> getMessagesForAdvertByAuthor(Advert advert, User author) {
        return messageRepository.findAllByAdvertAndAuthorOrRecipientOrderByIdDesc(advert, author, author);
    }

    @Override
    public List<Message> getAllRepliesForAdvert(Advert advert) {
        return messageRepository.findAllByAdvertOrderByIdDesc(advert);
    }
}
