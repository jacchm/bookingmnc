package com.mnc.booking.mapper;

import com.mnc.booking.controller.dto.user.UserCreationDTO;
import com.mnc.booking.controller.dto.user.UserDTO;
import com.mnc.booking.controller.dto.user.UserUpdateDTO;
import com.mnc.booking.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.mnc.booking.config.RoleConstants.GRAND_AUTHORITIES_SEPARATOR;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  @Mapping(target = "authorities", constant = "ROLE_USER")
  User mapToUser(final UserCreationDTO userCreationDTO);

  User mapToUser(final UserUpdateDTO userCreationDTO);

  @Mapping(target = "authorities", expression = "java(mapAuthoritiesListToString(user.getAuthorities()))")
  UserDTO mapToUserDTO(final User user);

  default String mapAuthoritiesListToString(final Collection<? extends GrantedAuthority> authorities) {
    return authorities
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(GRAND_AUTHORITIES_SEPARATOR));
  }

}
