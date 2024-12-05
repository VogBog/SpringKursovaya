package com.vogbog.seminars.security;

import com.vogbog.seminars.base.BooleanJson;
import com.vogbog.seminars.users.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/controller/")
@AllArgsConstructor
public class SecurityController {
    private final UsersRepository usersRepository;

    @GetMapping("/userExists/{login}")
    BooleanJson userExists(@PathVariable String login) {
        return new BooleanJson(usersRepository.existsByLogin(login));
    }
}
