package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.user.*;
import com.mnc.booking.exception.NotFoundException;
import com.mnc.booking.mapper.UserMapper;
import com.mnc.booking.model.User;
import com.mnc.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("users")
@RestController
public class UserController {

  private static final String USER_NOT_FOUND_ERROR_MSG = "User with username=%s has not been found.";
  private static final String FIELD_DIR_REGEX = "[-\\w]+(\\.[-\\w]+)*(:[a-zA-Z]+)?";
  private static final String SORT_REGEX = "^" + FIELD_DIR_REGEX + "(," + FIELD_DIR_REGEX + ")*$";
  private static final String X_TOTAL_COUNT = "X-TOTAL-COUNT";

  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping
  public ResponseEntity<UserCreateResponseDTO> createUser(@Valid @RequestBody final UserCreationDTO userCreationDTO) {
    log.info("Received creation request with user data={}", userCreationDTO);
    final String username = userService.createUser(userCreationDTO);
    return new ResponseEntity<>(UserCreateResponseDTO.of(username), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getUsers(@RequestHeader(name = X_TOTAL_COUNT, required = false, defaultValue = "false") final boolean xTotalCount,
                                                @RequestParam(required = false, defaultValue = "1") final Integer pageNumber,
                                                @RequestParam(required = false, defaultValue = "20") final Integer pageSize,
                                                @Pattern(regexp = SORT_REGEX, message = "Sort parameters should be in form of field:direction (ASC/DESC) separated by ',' (comma).")
                                                @RequestParam(required = false) final String sort,
                                                @Valid final UserFilterParams filterParams) {
    log.info("Received users search request with params: pageSize={}, pageNumber={}, xTotalCount={}, sortParams={} and filterParams={}", pageSize, pageNumber, xTotalCount, sort, filterParams);
    final Page<User> results = userService.searchUsers(pageNumber - 1, pageSize, sort, filterParams);
    final List<UserDTO> users = results.getContent()
        .stream()
        .map(userMapper::mapToUserDTO)
        .collect(Collectors.toList());

    final HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add(X_TOTAL_COUNT, xTotalCount ? String.valueOf(results.getTotalElements()) : null);

    return ResponseEntity.ok().headers(responseHeaders).body(users);
  }

  @GetMapping("{username}")
  public ResponseEntity<UserDTO> getUser(@PathVariable final String username) {
    log.info("Received user fetch request with username={}", username);
    final UserDTO userDTO = userService.getUser(username)
        .map(userMapper::mapToUserDTO)
        .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_ERROR_MSG, username)));

    return ResponseEntity.ok(userDTO);
  }

  @PatchMapping("{username}")
  public ResponseEntity<Void> updateUserRoles(@PathVariable final String username,
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
