package almeida.rafael.user_service.repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
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

  @Test
  @DisplayName("findById returns an user with given id")
  @Order(2)

  void findById_ReturnsUser_WhenSuccessfull() {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);

    var expectedUser = userList.getFirst();
    var users = repository.findById(expectedUser.getId());

    Assertions.assertThat(users).isPresent().contains(expectedUser);

  }

  @Test
  @DisplayName("findByFirstName returns an empty list when firsTame is null")
  @Order(3)

  void findByFirstName_ReturnsEmptyList_WhenNameIsNull() {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);

    var users = repository.findByFirstName(null);
    Assertions.assertThat(users).isNotNull().isEmpty();

  }

  @Test
  @DisplayName("findByFirstName returns list with found object when name exists")
  @Order(4)

  void findByFirstName_ReturnsFoundUserInList_WhenNameIsFound() {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);

    var expectedUser = userList.getFirst();
    var users = repository.findByFirstName(expectedUser.getFirstName());
    Assertions.assertThat(users).contains(expectedUser);
  }

  @Test
  @DisplayName("save creates a user")
  @Order(5)

  void save_CreatesUser_WhenSucessfull() {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);

    var userToSave = userUtils.newUserToSave();

    var user = repository.save(userToSave);
    Assertions.assertThat(user).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    var userSavedOptional = repository.findById(userToSave.getId());
    Assertions.assertThat(userSavedOptional).isPresent().contains(userToSave);
  }

  @Test
  @DisplayName("delete removes a user")
  @Order(6)
  void delete_RemoveUser_WhenSucessfull() {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);

    var userToDelete = userList.getFirst();
    repository.delete(userToDelete);

    var users = repository.findAll();
    Assertions.assertThat(users).isNotEmpty().doesNotContain(userToDelete);
  }

}
