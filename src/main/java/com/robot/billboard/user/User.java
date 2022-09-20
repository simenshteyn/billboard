package com.robot.billboard.user;

import com.robot.billboard.validator.OnCreate;
import com.robot.billboard.validator.OnLogin;
import com.robot.billboard.validator.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "Email should be in right format")
    @NotBlank(message = "Email can't be blank", groups = {OnCreate.class, OnLogin.class})
    @NotNull(message = "Email can't be null", groups = {OnCreate.class, OnLogin.class})
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Password can't be blank", groups = {OnCreate.class, OnLogin.class})
    @NotNull(message = "Password can't be null", groups = {OnCreate.class, OnLogin.class})
    @Column
    private String password;
    @ValueOfEnum(enumClass = UserRole.class)
    @NotBlank(message = "Role can't be blank", groups = OnCreate.class)
    @NotNull(message = "Role can't be null", groups = OnCreate.class)
    @Column
    private String role;
}
