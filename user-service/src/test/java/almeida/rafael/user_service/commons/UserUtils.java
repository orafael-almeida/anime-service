package almeida.rafael.user_service.commons;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import almeida.rafael.user_service.domain.User;

@Component
public class UserUtils {
  public List<User> newUserList() {
    var toyohisa = User.builder().id(1L).firstName("Toyohisa").lastName("Shimasu").email("toyohisa@drifters.com")
        .build();
    var ichigo = User.builder().id(2L).firstName("Ichigo").lastName("Kurosaki").email("ichigo@bleach.com").build();
    var ash = User.builder().id(3L).firstName("Ash").lastName("Ketchun").email("ash@pokemon.com").build();

    return new ArrayList<>(List.of(toyohisa, ichigo, ash));
  }

  public User newUserToSave() {
    return User.builder()
        .id(99L)
        .firstName("Yusuke")
        .lastName("Urameshi")
        .email("yusuke@yuyuhakusho.com")
        .build();
  }

}
