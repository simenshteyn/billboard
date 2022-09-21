package com.robot.billboard.advert;

import com.robot.billboard.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByAdvertAndAuthorOrRecipientOrderByIdDesc(Advert advert, User author, User recipient);

    List<Message> findAllByAdvertOrderByIdDesc(Advert advert);
}
