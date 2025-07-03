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

  void findById_ThrowsResponseStatusException_WhenProducerNotFound() {
    var expectedUser = userList.getFirst();
    BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedUser.getId()))
        .isInstanceOf(ResponseStatusException.class);
  }
}
