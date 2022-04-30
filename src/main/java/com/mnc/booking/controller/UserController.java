package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.user.UserCreationDTO;
import com.mnc.booking.controller.dto.user.UserDTO;
import com.mnc.booking.controller.dto.user.UserUpdateDTO;
import com.mnc.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("users")
@RestController
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<Void> registerUser(@Valid @RequestBody final UserCreationDTO userCreationDTO) {
    log.info("Received registration request with user data={}", userCreationDTO);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getUsers() {
    return ResponseEntity.ok(List.of());
  }

  //  @PreAuthorize(value = "hasRole('ROLE_ADMIN')") or ADMIN
//  @RolesAllowed(value = "ROLE_ADMIN")
  @GetMapping("{username}")
  public ResponseEntity<UserDTO> getUser(@PathVariable final String username) {
    log.info("Received user fetch request with username={}", username);
    return ResponseEntity.ok().build();
  }

  @PutMapping("{username}")
  public ResponseEntity<Void> updateUser(@PathVariable final String username,
                                         @RequestBody final UserUpdateDTO userUpdateDTO) {

    log.info("Received user update request with username={}", username);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable final String username) {
    log.info("Received user delete request with username={}", username);
    return ResponseEntity.ok().build();
  }

}
