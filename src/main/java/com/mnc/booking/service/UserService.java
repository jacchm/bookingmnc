package com.mnc.booking.service;

import com.mnc.booking.controller.dto.user.UserCreationDTO;
import com.mnc.booking.controller.dto.user.UserFilterParams;
import com.mnc.booking.controller.dto.user.UserRolesUpdateDTO;
import com.mnc.booking.controller.dto.user.UserUpdateDTO;
import com.mnc.booking.controller.util.SortParamsParser;
import com.mnc.booking.exception.AlreadyExistsException;
import com.mnc.booking.exception.NotFoundException;
import com.mnc.booking.mapper.UserMapper;
import com.mnc.booking.model.User;
import com.mnc.booking.repository.UserRepository;
import com.mnc.booking.util.GenericSpecificationsBuilder;
import com.mnc.booking.util.SpecificationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.mnc.booking.security.util.RoleConstants.GRAND_AUTHORITIES_SEPARATOR;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

  private static final String USER_ALREADY_EXISTS_ERROR_MSG = "User with username=%s already exists.";
  private static final String USER_NOT_FOUND_ERROR_MSG = "User with username=%s has not been found.";

  private final UserRepository userRepository;
  private final SpecificationFactory<User> userSpecificationFactory;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final SortParamsParser sortParamsParser;

  public String createUser(final UserCreationDTO userCreationDTO) {
    final User newUser = userMapper.mapToUser(userCreationDTO);
    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

    if (userRepository.existsById(userCreationDTO.getUsername())) {
      throw new AlreadyExistsException(String.format(USER_ALREADY_EXISTS_ERROR_MSG, userCreationDTO.getUsername()));
    }
    final User savedUser = userRepository.save(newUser);
    return savedUser.getUsername();
  }

  public Optional<User> getUser(final String username) {
    return userRepository.findById(username);
  }

  public Page<User> searchUsers(final Integer pageNumber, final Integer pageSize, final String sortParams,
                                final UserFilterParams filterParams) {

    final Sort sort = Sort.by(sortParamsParser.prepareSortOrderList(sortParams, User.class));
    final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    return userRepository.findAll(buildFilteringQuery(filterParams), pageable);
  }

  public void updateUser(final String username, final UserUpdateDTO userUpdateDTO) {
    final User userUpdate = userMapper.mapToUser(userUpdateDTO);
    final User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_ERROR_MSG, username)));
    BeanUtils.copyProperties(userUpdate, user, ignoreNullProperties(userUpdate));

    userRepository.save(user);
  }

  public void updatePassword(final String username, final String password, final String newPassword) {
    final User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_ERROR_MSG, username)));
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Wrong current password provided.");
    }
    user.setPassword(passwordEncoder.encode(newPassword));

    userRepository.save(user);
  }

  public void updateUserRoles(final String username, final UserRolesUpdateDTO userRolesUpdateDTO) {
    userRepository.setUserAuthoritiesByUsername(username, String.join(GRAND_AUTHORITIES_SEPARATOR, userRolesUpdateDTO.getAuthorities()))
        .filter(result -> result > 0)
        .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_ERROR_MSG, username)));
  }

  public void deleteUser(final String username) {
    userRepository.deleteById(username);
  }

  private Specification<User> buildFilteringQuery(final UserFilterParams filters) {
    final GenericSpecificationsBuilder<User> builder = new GenericSpecificationsBuilder<>();
    if (Objects.nonNull(filters)) {
      ReflectionUtils.doWithFields(UserFilterParams.class,
          field -> {
            field.setAccessible(true);
            if (Objects.nonNull(field.get(filters))) {
              if (!"role".equals(field.getName())) {
                builder.with(userSpecificationFactory.isEqual(field.getName(), field.get(filters), builder));
              } else {
                builder.with(userSpecificationFactory.isEqual("authorities", field.get(filters), builder));
              }
            }
          });
    }
    return builder.build();
  }

  private String[] ignoreNullProperties(final User userUpdate) {
    final List<String> ignoredProperties = new ArrayList<>();
    ReflectionUtils.doWithFields(User.class,
        field -> {
          if (Objects.isNull(field.get(userUpdate))) {
            ignoredProperties.add(field.getName());
          }
        });
    return ignoredProperties.toArray(String[]::new);
  }

}
