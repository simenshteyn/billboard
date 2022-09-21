package com.robot.billboard.advert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.robot.billboard.user.User;
import com.robot.billboard.validator.OnCreate;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Text can't be blank", groups = OnCreate.class)
    @NotNull(message = "Text can't be null", groups = OnCreate.class)
    @Column
    private String text;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    @JsonIgnore
    private User author;
    @Column(name = "author_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long authorId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_id", insertable = false, updatable = false)
    @JsonIgnore
    private User recipient;
    @Column(name = "recipient_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long recipientId;

    @ManyToOne(targetEntity = Advert.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "advert_id", insertable = false, updatable = false)
    @JsonIgnore
    private Advert advert;
    @Column(name = "advert_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long advertId;
    @Column
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;
}
