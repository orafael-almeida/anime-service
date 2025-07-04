package almeida.rafael.user_service.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import almeida.rafael.user_service.commons.UserUtils;
import almeida.rafael.user_service.domain.User;
import almeida.rafael.user_service.repository.UserHardCodedRepository;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {
  @InjectMocks
  private UserService service;
  @Mock
  private UserHardCodedRepository repository;
  private List<User> userList;
  @InjectMocks
  private UserUtils userUtils;

  @BeforeEach
  void init() {
    userList = userUtils.newUserList();
  }

  @Test
  @DisplayName("findAll returns a list with all users when firstName is null")
  @Order(1)
  void findAll_ReturnsAllUsers_WhenFirstNameIsNull() {
    BDDMockito.when(repository.findAll()).thenReturn(userList);

    var users = service.findAll(null);
    assertThat(users).isNotNull().hasSameElementsAs(userList);
  }

  @Test
  @DisplayName("findAll returns list with found objects when argument FirstName exists")
  @Order(2)
  void findByFirstName_ReturnsFoundUsersInList_WhenFirstNameIsFound() {
    var user = userList.getFirst();
    BDDMockito.when(repository.findByFirstName(user.getFirstName())).thenReturn(Collections.singletonList(user));

    var users = service.findAll(user.getFirstName());
    assertThat(users).containsAll(Collections.singletonList(user));
  }

  @Test
  @DisplayName("findAll returns empty list when FirstName is not found")
  @Order(3)
  void findByFirstName_ReturnsEmptyList_WhenFirstNameIsNotFound() {
    var name = "not-found";
    BDDMockito.when(repository.findByFirstName(name)).thenReturn(Collections.emptyList());

    var users = service.findAll(name);
    assertThat(users).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("findById returns a user with given id")
  @Order(4)

  void findById_ReturnsUser_WhenSuccessfull() {
    var expectedUser = userList.getFirst();
    BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

    var user = service.findByIdOrThrowNotFound(expectedUser.getId());

    Assertions.assertThat(user).isEqualTo(expectedUser);

  }

  @Test
  @DisplayName("findById throws ResponseStatusException when user is not found")
  @Order(5)

  void findById_ThrowsResponseStatusException_WhenUserNotFound() {
    var expectedUser = userList.getFirst();
    BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedUser.getId()))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("save creates a user")
  @Order(6)

  void save_CreatesUser_WhenSucessfull() {
    var userToSave = userUtils.newUserToSave();
    BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);

    var savedUser = service.save(userToSave);
    Assertions.assertThat(savedUser).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
  }

  @Test
  @DisplayName("delete removes a user")
  @Order(7)
  void delete_RemoveUser_WhenSucessfull() {
    var userToDelete = userList.getFirst();

    BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
    BDDMockito.doNothing().when(repository).delete(userToDelete);
    Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));

  }

  @Test
  @DisplayName("delete throws ResponseStatusException when user is not found")
  @Order(8)
  void delete_ThrowsResponseStatusException_WhenUserIsNotFound() {
    var userToDelete = userList.getFirst();

    BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.delete(userToDelete.getId()))
        .isInstanceOf(ResponseStatusException.class);

  }

  @Test
  @DisplayName("update updates a user")
  @Order(9)

  void update_UpdatesUser_WhenSucessfull() {
    var userToUpdate = userList.getFirst();
    userToUpdate.setFirstName("Inuyasha");

    BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
    BDDMockito.doNothing().when(repository).update(userToUpdate);

    Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));

  }

  @Test
  @DisplayName("update throws ResponseStatusException when user is not found")
  @Order(10)

  void update_ThrowsResponseStatusException_WhenUserIsNotFound() {
    var userToUpdate = userList.getFirst();
    BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.update(userToUpdate))
        .isInstanceOf(ResponseStatusException.class);

  }
}
