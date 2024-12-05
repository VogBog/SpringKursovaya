package com.vogbog.seminars.users;

import com.vogbog.seminars.userEvent.UserEvent;
import com.vogbog.seminars.userEvent.UserEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class UsersService {
    private final UsersRepository repository;

    public SecuredUser getUser(Long id) {
        return SecuredUser.from(repository.findById(id).orElseThrow());
    }

    public SecuredUser getByLogin(String login) {
        return SecuredUser.from(repository.findByLogin(login).orElseThrow());
    }

    public List<SecuredUser> getByEventId(Long id) {
        return repository.findByEventId(id).stream().map(SecuredUser::from).toList();
    }

    public Boolean setRole(Long userId, Role role, Principal principal) {
        UserAccount admin = repository.findByLogin(principal.getName()).orElseThrow();
        if(admin.getMyRole().value < role.value) {
            return false;
        }
        UserAccount user = repository.findById(userId).orElseThrow();
        if(admin.getMyRole().value < user.getMyRole().value) {
            return false;
        }
        user.setMyRole(role);
        repository.save(user);
        return true;
    }
}
