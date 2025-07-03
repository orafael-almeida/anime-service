package almeida.rafael.user_service.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
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
  @DisplayName("GET v1/users/1 throws ResponseStatusException 404 when user is not found")
  @Order(5)

  void findById_ThrowsResponseStatusException_WhenUserNotFound() throws Exception {
    BDDMockito.when(userData.getUsers()).thenReturn(userList);
    var id = 99L;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.status().reason("User not found"));
  }
}
