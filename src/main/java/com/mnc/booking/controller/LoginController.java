package com.mnc.booking.controller;

import com.mnc.booking.controller.dto.auth.AuthRequestDTO;
import com.mnc.booking.controller.dto.auth.AuthResponseDTO;
import com.mnc.booking.controller.dto.user.UserCreateResponseDTO;
import com.mnc.booking.controller.dto.user.UserCreationDTO;
import com.mnc.booking.security.util.JwtTokenProvider;
import com.mnc.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class LoginController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody final AuthRequestDTO authRequest) {
    log.info("Logging user with credentials={}", authRequest);
    final Authentication authenticate = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
    final String jwt = jwtTokenProvider.createToken(authenticate);

    return new ResponseEntity<>(AuthResponseDTO.of(jwt), HttpStatus.OK);
  }

  @PostMapping("/register")
  public ResponseEntity<UserCreateResponseDTO> registerUser(@Valid @RequestBody final UserCreationDTO userCreationDTO) {
    log.info("Received registration request with user data={}", userCreationDTO);
    final String username = userService.createUser(userCreationDTO);
    return new ResponseEntity<>(UserCreateResponseDTO.of(username), HttpStatus.CREATED);
  }

}
