package almeida.rafael.user_service.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import almeida.rafael.user_service.domain.User;
import almeida.rafael.user_service.repository.UserHardCodedRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserHardCodedRepository repository;

  public List<User> findAll(String name) {
    return name == null ? repository.findAll() : repository.findByFirstName(name);
  }

  public User findByIdOrThrowNotFound(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  public User save(User user) {
    return repository.save(user);
  }

  public void delete(Long id) {
    var user = findByIdOrThrowNotFound(id);
    repository.delete(user);
  }

  public void update(User userToUpdate) {
    var user = findByIdOrThrowNotFound((userToUpdate.getId()));
    repository.update(user);
  }
}
