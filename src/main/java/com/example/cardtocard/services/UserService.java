package com.example.cardtocard.services;

import com.example.cardtocard.models.Roles;
import com.example.cardtocard.models.User;
import com.example.cardtocard.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    public void addUser(User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setPatronymic(user.getPatronymic());
        newUser.setSurname(user.getSurname());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        newUser.setActive(true);
        List<Roles> userRole = List.of(Roles.USER);
        newUser.setRole(userRole);
        userRepository.save(newUser);
    }

    public boolean confirmPassword(User user) {
        if (user.getPassword().equals(user.getConfirm())) {
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username);
    }


    public User loadUserById(Long id){
        return userRepository.getUserById(id);
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    //private List<GrantedAuthority> getAuthorities(User user) {
    //    List<GrantedAuthority> authorities = new ArrayList<>();
    //    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().get(0)));
    //    return authorities;
    //}
}
