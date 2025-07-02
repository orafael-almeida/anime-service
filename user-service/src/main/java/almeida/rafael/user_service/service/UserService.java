package almeida.rafael.user_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import almeida.rafael.user_service.domain.User;
import almeida.rafael.user_service.repository.UserHardCodeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserHardCodeRepository repository;

  public List<User> findAll(String name) {
    return name == null ? repository.findAll() : repository.findByName(name);
  }
}
