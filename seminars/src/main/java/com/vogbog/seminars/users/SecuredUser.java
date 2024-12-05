package com.vogbog.seminars.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecuredUser implements Serializable {
    private Long id;
    private String login;
    private String name;

    public static SecuredUser from(UserAccount user) {
        return new SecuredUser(user.getId(), user.getLogin(), user.getName());
    }
}
