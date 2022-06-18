package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.user.UserDTO;
import com.mnc.booking.controller.dto.user.UserPasswordUpdateDTO;
import com.mnc.booking.controller.dto.user.UserUpdateDTO;
import com.mnc.booking.exception.NotFoundException;
import com.mnc.booking.mapper.UserMapper;
import com.mnc.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/me")
public class MeController {

  private static final String USER_NOT_FOUND_ERROR_MSG = "User with username=%s has not been found.";

  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping
  public ResponseEntity<UserDTO> getMe(@RequestHeader final String username) {
    log.info("User with username={} get request.", username);
    final UserDTO userDTO = userService.getUser(username)
        .map(userMapper::mapToUserDTO)
        .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_ERROR_MSG, username)));
    return ResponseEntity.ok(userDTO);
  }

  @PutMapping
  public ResponseEntity<Void> updateMe(@RequestHeader final String username,
                                       @Valid @RequestBody final UserUpdateDTO userUpdateDTO) {
    log.info("User with username={} update request with update={}", username, userUpdateDTO);
    userService.updateUser(username, userUpdateDTO);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping
  public ResponseEntity<Void> resetPassword(@RequestHeader final String username,
                                            @Valid @RequestBody final UserPasswordUpdateDTO userPasswordUpdateDTO) {
    log.info("Resetting password for user with username={}", username);
    userService.updatePassword(username, userPasswordUpdateDTO.getPassword(), userPasswordUpdateDTO.getNewPassword());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteMe(@RequestHeader final String username) {
    userService.deleteUser(username);
    return ResponseEntity.noContent().build();
  }

}
