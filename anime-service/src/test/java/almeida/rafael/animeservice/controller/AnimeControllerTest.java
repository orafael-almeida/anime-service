package almeida.rafael.animeservice.controller;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import almeida.rafael.animeservice.commons.AnimeUtils;
import almeida.rafael.animeservice.commons.FileUtils;
import almeida.rafael.animeservice.domain.Anime;
import almeida.rafael.animeservice.repository.AnimeData;
import almeida.rafael.animeservice.repository.AnimeHardCodedRepository;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "almeida.rafael")
public class AnimeControllerTest {
  private static final String URL = "/v1/animes";
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private AnimeData animeData;
  @MockitoSpyBean
  private AnimeHardCodedRepository repository;
  private List<Anime> animesList;
  @Autowired
  private FileUtils fileUtils;
  @Autowired
  private AnimeUtils animeUtils;

  @BeforeEach
  void init() {
    animesList = animeUtils.newAnimeList();
  }

  @Test
  @DisplayName("GET v1/animes returns a list with all animes when argument name is null")
  @Order(1)
  void findAll_ReturnsAllAnimes_WhenArgumentIsNull() throws Exception {

    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes?name=ufotable returns list with found objects when argument name exists")
  @Order(2)
  void findAll_ReturnsFoundAnimesInList_WhenNameIsFound() throws Exception {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var response = fileUtils.readResourceFile("anime/get-anime-ufotable-name-200.json");
    var name = "Mashle";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name",
        name))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes?name=x returns empty list when name is not found")
  @Order(3)
  void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");
    var name = "x";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name",
        name))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes/1 returns a anime with given id")
  @Order(4)

  void findById_ReturnsAnimes_WhenSuccessfull() throws Exception {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var response = fileUtils.readResourceFile("anime/get-anime-by-id-200.json");
    var id = 1L;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes/1 throws anime is not found")
  @Order(5)

  void findById_ThrowsNotFound_WhenAnimeNotFound() throws Exception {
    var response = fileUtils.readResourceFile("anime/get-anime-by-id-404.json");
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var id = 99L;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().json(response));

  }

  @Test
  @DisplayName("POST v1/animes creates a anime")
  @Order(6)
  void save_CreatesAnime_WhenSucessfull() throws Exception {
    var request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
    var response = fileUtils.readResourceFile("anime/post-response-anime-201.json");
    var animeToSave = animeUtils.newAnimeToSave();
    BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

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
  @DisplayName("PUT v1/animes updates a anime")
  @Order(7)
  void update_UpdatesAnime_WhenSucessfull() throws Exception {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var request = fileUtils.readResourceFile("anime/put-request-anime-200.json");

    mockMvc.perform(MockMvcRequestBuilders
        .put(URL)
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());

  }

  @Test
  @DisplayName("PUT v1/animes throws NotFound when anime is not found")
  @Order(8)

  void update_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception {
    var response = fileUtils.readResourceFile("anime/put-anime-by-id-404.json");

    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");

    mockMvc.perform(MockMvcRequestBuilders
        .put(URL)
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().json(response));

  }

  @Test
  @DisplayName("DELETE v1/animes/1 removes a anime by Id")
  @Order(9)
  void delete_RemoveAnime_WhenSucessfull() throws Exception {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

    var id = animesList.getFirst().getId();
    mockMvc.perform(MockMvcRequestBuilders
        .delete(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("DELETE v1/animes/{id} throws NotFound when anime is not found")
  @Order(10)
  void delete_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception {
    var response = fileUtils.readResourceFile("anime/delete-anime-by-id-404.json");
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

    var id = 99L;
    mockMvc.perform(MockMvcRequestBuilders
        .delete(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().json(response));

  }
}
