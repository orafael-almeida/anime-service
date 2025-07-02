package almeida.rafael.user_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import almeida.rafael.user_service.mapper.UserMapper;
import almeida.rafael.user_service.response.UserGetResponse;
import almeida.rafael.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
  private final UserService service;
  private final UserMapper mapper;

  @GetMapping
  public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam String firstName) {
    log.debug("Request received to list all users, param firstName '{}'", firstName);

    var users = service.findAll(firstName);

    var userGetResponses = mapper.toUserGetResponseList(users);

    return ResponseEntity.ok(userGetResponses);
  }

}
