package almeida.rafael.user_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import almeida.rafael.user_service.domain.User;
import almeida.rafael.exception.NotFoundException;
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
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  public User save(User user) {
    return repository.save(user);
  }

  public void delete(Long id) {
    var user = findByIdOrThrowNotFound(id);
    repository.delete(user);
  }

  public void update(User userToUpdate) {
    assertUserExists(userToUpdate.getId());
    repository.update(userToUpdate);
  }

  public void assertUserExists(Long id) {
    findByIdOrThrowNotFound(id);
  }
}
