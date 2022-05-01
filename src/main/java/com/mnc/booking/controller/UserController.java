package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.user.UserCreateResponseDTO;
import com.mnc.booking.controller.dto.user.UserCreationDTO;
import com.mnc.booking.controller.dto.user.UserDTO;
import com.mnc.booking.controller.dto.user.UserRolesUpdateDTO;
import com.mnc.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<UserCreateResponseDTO> createUser(@Valid @RequestBody final UserCreationDTO userCreationDTO) {
    log.info("Received creation request with user data={}", userCreationDTO);
    final String username = userService.createUser(userCreationDTO);
    return new ResponseEntity<>(UserCreateResponseDTO.of(username), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getUsers(@RequestHeader(name = "X-TOTAL-COUNT", required = false, defaultValue = "false") final boolean xTotalCount,
                                                @RequestParam(required = false, defaultValue = "1") final Integer pageNumber,
                                                @RequestParam(required = false, defaultValue = "10") final Integer pageSize,
                                                @RequestParam(required = false, defaultValue = "") final String sort) {
    log.info("Received users fetch request with params: pageSize={}, pageNumber={}, xTotalCount={} and sort={}", pageSize, pageNumber, xTotalCount, sort);
    final List<UserDTO> users = userService.getUsers(pageNumber - 1, pageSize, sort, xTotalCount);
    return ResponseEntity.ok(users);
  }

  @GetMapping("{username}")
  public ResponseEntity<UserDTO> getUser(@PathVariable final String username) {
    log.info("Received user fetch request with username={}", username);
    final UserDTO userDTO = userService.getUser(username);
    return ResponseEntity.ok(userDTO);
  }

  @PatchMapping("{username}")
  public ResponseEntity<Void> updateUser(@PathVariable final String username,
                                         @RequestBody final UserRolesUpdateDTO userRolesUpdateDTO) {
    log.info("Received user update request with username={}", username);
    userService.updateUserRoles(username, userRolesUpdateDTO);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable final String username) {
    log.info("Received user delete request with username={}", username);
    userService.deleteUser(username);
    return ResponseEntity.noContent().build();
  }

}
