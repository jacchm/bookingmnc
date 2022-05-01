package com.mnc.booking.service;

import com.mnc.booking.controller.dto.user.UserCreationDTO;
import com.mnc.booking.controller.dto.user.UserDTO;
import com.mnc.booking.controller.dto.user.UserRolesUpdateDTO;
import com.mnc.booking.controller.dto.user.UserUpdateDTO;
import com.mnc.booking.exception.AlreadyExistsException;
import com.mnc.booking.exception.NotFoundException;
import com.mnc.booking.mapper.UserMapper;
import com.mnc.booking.model.User;
import com.mnc.booking.repository.UserRepository;
import com.mnc.booking.util.SortParamsParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mnc.booking.config.RoleConstants.GRAND_AUTHORITIES_SEPARATOR;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

  private static final String USER_ALREADY_EXISTS_ERROR_MSG = "User with username=%s already exists.";
  private static final String USER_NOT_FOUND_ERROR_MSG = "User with username=%s has not been found.";

  private final UserRepository userRepository;
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

  public UserDTO getUser(final String username) {
    return userRepository.findById(username)
        .map(userMapper::mapToUserDTO)
        .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_ERROR_MSG, username)));
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

  public List<UserDTO> getUsers(final Integer pageNumber, final Integer pageSize, final String sortParams, final Boolean xTotalCount) {
    // TODO: add sort parsing
    final Sort sort = Sort.by(sortParamsParser.prepareSortOrderList(sortParams));
    final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    return userRepository.findAll(pageable)
        .getContent()
        .stream()
        .map(userMapper::mapToUserDTO)
        .collect(Collectors.toList());
  }

  public void deleteUser(final String username) {
    userRepository.deleteById(username);
  }

  private String[] ignoreNullProperties(final User userUpdate) {
    final List<String> ignoredProperties = new ArrayList<>();
    ignoredProperties.add("username");
    ignoredProperties.add("password");
    ignoredProperties.add("authorities");
    if (Objects.isNull(userUpdate.getEmail())) {
      ignoredProperties.add("email");
    }
    if (Objects.isNull(userUpdate.getName())) {
      ignoredProperties.add("name");
    }
    if (Objects.isNull(userUpdate.getSurname())) {
      ignoredProperties.add("surname");
    }

    return ignoredProperties.toArray(String[]::new);
  }

//  private User ignoreNullPropertiesOM(final User userUpdate, final User user) throws IOException {
//    // problem with authorities as a list (probably getter is messing up)
//    final JsonNode jsonNode = objectMapper.valueToTree(user);
//    ObjectReader updater = objectMapper.readerForUpdating(userUpdate);
//    return updater.readValue(jsonNode, User.class);
//  }

}
