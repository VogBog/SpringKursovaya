package com.vogbog.seminars.security;

import com.vogbog.seminars.users.UserAccount;
import com.vogbog.seminars.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUsersService  implements UserDetailsService {
    private UsersRepository repository;

    @Autowired
    public void setUserRepository(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = repository.findByLogin(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User %s not found", username)));
        return UserDetailsImplementation.build(user);
    }
}
