package ru.kata.spring.boot_security.demo.model;



import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;


   @Column(name = "firstName")
   private String firstName;

   @Column(name = "lastName")
   private String lastName;

   @Column(name = "email")
   private String email;

   @Column(name = "password")
   private String password;

   @Transient
   private List<SimpleGrantedAuthority> authorities;

   @ManyToMany (targetEntity = Role.class, fetch = FetchType.LAZY)
   @JoinTable(name = "user_roles", joinColumns =  @JoinColumn(name = "user_id"),
           inverseJoinColumns =  @JoinColumn(name = "role_id"))
   private List<Role> roles;

   public User(String firstName, String password, List<SimpleGrantedAuthority> authorities) {
      this.firstName = firstName;
      this.password = password;
      this.authorities = authorities;
   }

   public User() {

   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {

      return getRoles();
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return firstName;
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
