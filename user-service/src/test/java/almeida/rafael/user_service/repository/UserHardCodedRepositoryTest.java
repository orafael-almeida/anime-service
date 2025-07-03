package almeida.rafael.user_service.repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import almeida.rafael.user_service.commons.UserUtils;
import almeida.rafael.user_service.domain.User;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserHardCodedRepositoryTest {
  @InjectMocks
  private UserHardCodedRepository repository;

  @Mock
  private UserData userData;
  private List<User> userList;
  @InjectMocks
  private UserUtils userUtils;

  @BeforeEach
  void init() {
    userList = userUtils.newUserList();
  }

  @Test
  @DisplayName("findAll returns a list with all users")
  @Order(1)

  void findAll_ReturnsAllUsers_WhenSuccessfull() {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);
    var users = repository.findAll();
    assertThat(users).isNotNull().hasSameElementsAs(userList);

  }
}
