package almeida.rafael.user_service.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import almeida.rafael.user_service.commons.FileUtils;
import almeida.rafael.user_service.commons.UserUtils;
import almeida.rafael.user_service.domain.User;
import almeida.rafael.user_service.repository.UserData;
import almeida.rafael.user_service.repository.UserHardCodedRepository;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "almeida.rafael")
public class UserControllerTest {
  private static final String URL = "/v1/users";
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private UserData userData;
  @MockitoSpyBean
  private UserHardCodedRepository repository;
  private List<User> userList;
  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private UserUtils userUtils;

  @BeforeEach
  void init() {
    userList = userUtils.newUserList();
  }

  @Test
  @DisplayName("GET v1/users returns a list with allusers when argument firstName is null")
  @Order(1)
  void findAll_ReturnsAllUser_WhenArgumentIsNull() throws Exception {

    BDDMockito.when(userData.getUsers()).thenReturn(userList);
    var response = fileUtils.readResourceFile("user/get-user-null-first-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/users?firstName=Toyohisa returns list with found objects when argument firstName exists")
  @Order(2)
  void findAll_ReturnsFoundUsersInList_WhenFirstNameIsFound() throws Exception {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);
    var response = fileUtils.readResourceFile("user/get-user-toyohisa-first-name-200.json");
    var firstName = "Toyohisa";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/users?firstName=x returns empty list when name is not found")
  @Order(3)
  void findAll_ReturnsEmptyList_WhenFirstNameIsNotFound() throws Exception {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);
    var response = fileUtils.readResourceFile("user/get-user-x-first-name-200.json");
    var firstName = "x";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/users/1 returns a user with given id")
  @Order(4)

  void findById_ReturnsUser_WhenSuccessfull() throws Exception {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);
    var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");
    var id = 1L;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/users/1 throws NotFound 404 when user is not found")
  @Order(5)

  void findById_ThrowsNotFound_WhenUserNotFound() throws Exception {
    var response = fileUtils.readResourceFile("user/get-user-by-id-404.json");

    BDDMockito.when(userData.getUsers()).thenReturn(userList);
    var id = 99L;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("POST v1/users creates a user")
  @Order(6)
  void save_CreatesUser_WhenSucessfull() throws Exception {
    var request = fileUtils.readResourceFile("user/post-request-user-200.json");
    var response = fileUtils.readResourceFile("user/post-response-user-201.json");
    var userToSave = userUtils.newUserToSave();
    BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);

    mockMvc.perform(MockMvcRequestBuilders
        .post(URL)
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("DELETE v1/users/1 removes a user by Id")
  @Order(7)
  void delete_RemoveUser_WhenSucessfull() throws Exception {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);

    var id = userList.getFirst().getId();
    mockMvc.perform(MockMvcRequestBuilders
        .delete(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("DELETE v1/users/{id} throws NotFound when user is not found")
  @Order(8)
  void delete_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);
    var response = fileUtils.readResourceFile("user/delete-user-by-id-404.json");

    var id = 99L;
    mockMvc.perform(MockMvcRequestBuilders
        .delete(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().json(response));

  }

  @Test
  @DisplayName("PUT v1/users updates a user")
  @Order(9)
  void update_UpdatesUser_WhenSucessfull() throws Exception {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);
    var request = fileUtils.readResourceFile("user/put-request-user-200.json");

    mockMvc.perform(MockMvcRequestBuilders
        .put(URL)
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());

  }

  @Test
  @DisplayName("PUT v1/users throws NotFound when user is not found")
  @Order(10)

  void update_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);
    var request = fileUtils.readResourceFile("user/put-request-user-404.json");
    var response = fileUtils.readResourceFile("user/put-user-by-id-404.json");

    mockMvc.perform(MockMvcRequestBuilders
        .put(URL)
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().json(response));

  }

  @ParameterizedTest
  @MethodSource("postUserBadRequestSource")
  @DisplayName("POST v1/users returns bad request when fields are invalid")
  @Order(11)
  void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
    var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post(URL)
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

    var resolvedException = mvcResult.getResolvedException();

    Assertions.assertThat(resolvedException).isNotNull();

    Assertions.assertThat(resolvedException.getMessage()).contains(errors);
  }

  @ParameterizedTest
  @MethodSource("putUserBadRequestSource")
  @DisplayName("PUT v1/users returns bad request when fields are invalid")
  @Order(12)
  void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
    var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .put(URL)
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

    var resolvedException = mvcResult.getResolvedException();

    Assertions.assertThat(resolvedException).isNotNull();

    Assertions.assertThat(resolvedException.getMessage()).contains(errors);
  }

  private static Stream<Arguments> postUserBadRequestSource() {
    var allRequiredErrors = allRequiredErrors();
    var emailInvalidError = invalidEmailErrors();

    return Stream.of(
        Arguments.of("post-request-user-empty-fields-400.json", allRequiredErrors),
        Arguments.of("post-request-user-blank-fields-400.json", allRequiredErrors),
        Arguments.of("post-request-user-invalid-email-400.json", emailInvalidError));
  }

  private static Stream<Arguments> putUserBadRequestSource() {
    allRequiredErrors().add("The field 'id' cannot be null");
    var allErrors = allRequiredErrors();
    var emailError = invalidEmailErrors();

    return Stream.of(
        Arguments.of("put-request-user-empty-fields-400.json", allErrors),
        Arguments.of("put-request-user-blank-fields-400.json", allErrors),
        Arguments.of("put-request-user-invalid-email-400.json", emailError));
  }

  private static List<String> invalidEmailErrors() {
    var emailInvalidError = "The e- mail is not valid";

    return List.of(emailInvalidError);
  }

  private static List<String> allRequiredErrors() {
    var firstNameRequiredError = "The field 'firstName' is required";
    var lastNameRequiredError = "The field 'lastName' is required";
    var emailRequiredError = "The field 'email' is required";

    return new ArrayList<>(List.of(firstNameRequiredError, lastNameRequiredError, emailRequiredError));
  }
}
