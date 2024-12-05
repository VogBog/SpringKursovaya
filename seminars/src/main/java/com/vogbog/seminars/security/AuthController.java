package com.vogbog.seminars.security;

import com.vogbog.seminars.security.dto.SignInRequest;
import com.vogbog.seminars.security.dto.SignUpRequest;
import com.vogbog.seminars.users.Role;
import com.vogbog.seminars.users.UserAccount;
import com.vogbog.seminars.users.UsersRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;

    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        if (usersRepository.existsByLogin(request.getLogin())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different login");
        }
        String hashed = passwordEncoder.encode(request.getPassword());

        try {
            UserAccount user = new UserAccount();
            user.setName(request.getUsername());
            user.setLogin(request.getLogin());
            user.setPassword(hashed);
            user.setMyRole(Role.User);
            usersRepository.save(user);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username, login and password must be not null");
        }


        return ResponseEntity.ok("Success");
    }

    @PostMapping("/signin")
    ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getLogin(), request.getPassword()
            ));
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login or password are incorrect");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }
}
