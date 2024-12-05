package com.vogbog.seminars.users;

import com.vogbog.seminars.userEvent.UserEvent;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("secured/users")
@AllArgsConstructor
public class UsersController {
    private final UsersService service;

    @GetMapping("/{id}")
    SecuredUser getUser(@PathVariable Long id) {
        return service.getUser(id);
    }

    @GetMapping("/byLogin/{login}")
    SecuredUser getUserByLogin(@PathVariable String login) {
        return service.getByLogin(login);
    }

    @GetMapping("/byEvent/{id}")
    List<SecuredUser> getUsersByEvent(@PathVariable Long id) {
        return service.getByEventId(id);
    }

    @PutMapping("/setRole")
    ResponseEntity<?> setRole(@RequestParam Long userId, @RequestParam Integer role, Principal principal) {
        if(service.setRole(userId, Role.valueOf(role), principal)) {
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }
}
