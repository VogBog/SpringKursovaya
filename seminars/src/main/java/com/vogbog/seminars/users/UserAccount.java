package com.vogbog.seminars.users;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="user_account")
@Data
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column private String login;
    @Column private String name;
    @Column private String password;
    @Enumerated(EnumType.STRING) @Column private Role myRole;
}