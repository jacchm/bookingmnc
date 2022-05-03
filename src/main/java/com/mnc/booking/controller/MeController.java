package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.user.UserPasswordUpdateDTO;
import com.mnc.booking.controller.dto.user.UserUpdateDTO;
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

  private final UserService userService;

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
