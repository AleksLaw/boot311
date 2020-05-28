package main.config;

import main.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        main.model.User myUser = userRepo.findByName(userName);
        if (myUser == null) {
            throw new UsernameNotFoundException("Unknown user: " + userName);
        }
        UserDetails user = User.withDefaultPasswordEncoder()
                .username(myUser.getName())
                .password(myUser.getPassword())
                .roles(myUser
                        .getUserRoles()
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")))
                .build();
        return user;
    }
}