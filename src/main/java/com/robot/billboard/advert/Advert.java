package com.robot.billboard.advert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robot.billboard.user.User;
import com.robot.billboard.validator.OnCreate;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "adverts")
@Data
@NoArgsConstructor
public class Advert {
    @Id
    @Column(name = "advert_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name can't be blank", groups = OnCreate.class)
    @NotNull(message = "Name can't be null", groups = OnCreate.class)
    @Column
    private String name;
    @NotBlank(message = "Description can't be blank", groups = OnCreate.class)
    @NotNull(message = "Description can't be null", groups = OnCreate.class)
    @Column
    private String description;
    @NotBlank(message = "Phone can't be blank", groups = OnCreate.class)
    @NotNull(message = "Phone can't be null", groups = OnCreate.class)
    @Column
    private String phone;
    @NotNull(message = "Available can't be null", groups = OnCreate.class)
    @Column
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore
    private User owner;
}
