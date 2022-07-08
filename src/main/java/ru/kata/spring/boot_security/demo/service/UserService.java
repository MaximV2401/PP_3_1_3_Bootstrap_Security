package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.*;


@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder bcryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder bcryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }


    public List<User> listUsers() {
        return userRepository.findAll();
    }


//    public void add(User user) {
//        userRepository.save(user);
//    }

    public Boolean saveUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        user.setRoles(Collections.singletonList(new Role(1L,"ROLE_USER")));
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }


    public User getUser(long id) {
        return userRepository.getById(id);
    }



    public void delete(long id) {
        userRepository.deleteById(id);
    }


    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;

//        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
//
//        return new org.springframework.security.core.userdetails.User(user.getUsername(),
//                user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(),  authorities);
    }
    private List<GrantedAuthority> getUserAuthority(List<Role> userRoles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (Role role : userRoles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return grantedAuthorities;
    }
//    public static UserDetails fromUser(User user, List<GrantedAuthority> authorities) {
//        return new org.springframework.security.core.userdetails.User(user.getUsername(),
//                user.getPassword(), true, true, true, true,  authorities);
//    }

}
