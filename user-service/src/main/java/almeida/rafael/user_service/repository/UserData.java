package almeida.rafael.user_service.repository;

import java.util.ArrayList;
import java.util.List;

import almeida.rafael.user_service.domain.User;

public class UserData {
  private final List<User> users = new ArrayList<>(3);

  {
    var goku = User.builder().id(1L).firstName("Goku").lastName("Son").email("goku@dbz.com").build();
    var gohan = User.builder().id(2L).firstName("Gohan").lastName("Son").email("gohan@dbz.com").build();
    var goten = User.builder().id(3L).firstName("Goten").lastName("Son").email("goten@dbz.com").build();

    users.addAll(List.of(goku, gohan, goten));
  }

  public List<User> getUsers() {
    return users;
  }
}
