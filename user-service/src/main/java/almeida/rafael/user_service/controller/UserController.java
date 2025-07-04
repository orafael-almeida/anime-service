package almeida.rafael.user_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import almeida.rafael.user_service.mapper.UserMapper;
import almeida.rafael.user_service.request.UserPostRequest;
import almeida.rafael.user_service.request.UserPutRequest;
import almeida.rafael.user_service.response.UserGetResponse;
import almeida.rafael.user_service.response.UserPostResponse;
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
  public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String firstName) {
    log.debug("Request received to list all users, param firstName '{}'", firstName);

    var users = service.findAll(firstName);

    var userGetResponses = mapper.toUserGetResponseList(users);

    return ResponseEntity.ok(userGetResponses);
  }

  @GetMapping("{id}")
  public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find user by id: {}", id);

    var user = service.findByIdOrThrowNotFound(id);
    var userGetResponse = mapper.toUserGetResponse(user);
    return ResponseEntity.ok(userGetResponse);
  }

  @PostMapping()
  public ResponseEntity<UserPostResponse> save(@RequestBody UserPostRequest request) {
    log.debug("Request to save a user: {}", request);

    var user = mapper.toUser(request);
    var userSaved = service.save(user);
    var userGetResponse = mapper.toUserPostResponse(userSaved);

    return ResponseEntity.status(HttpStatus.CREATED).body(userGetResponse);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    log.debug("Request do delete user by id: {}", id);

    service.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> updateUser(@RequestBody UserPutRequest request) {
    log.debug("Request to update user {}", request);

    var userToUpdated = mapper.toUser(request);
    service.update(userToUpdated);

    return ResponseEntity.noContent().build();
  }
}
