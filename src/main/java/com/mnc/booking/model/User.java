package com.mnc.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appuser",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    })
@Entity
public class User implements UserDetails {

  private static final String GRAND_AUTHORITIES_SEPARATOR = ",";

  @Id
  @Column(updatable = false)
  private String username;
  private String email;
  private String password;
  private String name;
  private String surname;
  private LocalDate dateOfBirth;
  private String authorities;
  private String phoneNumber;
  private String photoURI;
  @Version
  private int version;
  @Column(updatable = false)
  private Instant createdAt;
  @UpdateTimestamp
  private Instant modifiedAt;


  public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.authorities = authorities
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(GRAND_AUTHORITIES_SEPARATOR));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(authorities.split(GRAND_AUTHORITIES_SEPARATOR))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
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

  @PrePersist
  public void prePersistCreatedAt() {
    this.createdAt = Instant.now();
  }
}
