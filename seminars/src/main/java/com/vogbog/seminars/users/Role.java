package com.vogbog.seminars.users;

import java.util.Objects;

public enum Role {
    User(0),
    Moderator(1),
    Admin(2);

    public final Integer value;

    Role(Integer value) {
        this.value = value;
    }

    public static Role valueOf(Integer value) {
        for(Role role : Role.values()) {
            if(Objects.equals(role.value, value)) {
                return role;
            }
        }
        return User;
    }
}
