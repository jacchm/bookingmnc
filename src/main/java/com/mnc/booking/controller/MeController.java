package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.user.UserUpdateDTO;
import com.mnc.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

// TODO: filter uniemożliwiający zmianę innego użytkownika
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class MeController {

  private final UserService userService;

  @PatchMapping
  public ResponseEntity<Void> updateMe(@Valid @RequestBody final UserUpdateDTO userUpdateDTO) {
    log.info("Updating user with username={}", "hardcoded_username");

    return null;
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteMe(@RequestHeader final String username) {
    userService.deleteUser(username);
    return ResponseEntity.noContent().build();
  }

}
