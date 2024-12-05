package com.vogbog.seminars.security;

import com.vogbog.seminars.users.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
public class UserDetailsImplementation implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String login;

    public static UserDetailsImplementation build(UserAccount user) {
        return new UserDetailsImplementation(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getLogin()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
