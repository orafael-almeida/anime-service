package almeida.rafael.animeservice.controller;

import java.nio.file.Files;
import java.util.ArrayList;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import almeida.rafael.animeservice.domain.Anime;
import almeida.rafael.animeservice.repository.AnimeData;
import almeida.rafael.animeservice.repository.AnimeHardCodedRepository;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "almeida.rafael")
public class AnimeControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private AnimeData animeData;
  @MockitoSpyBean
  private AnimeHardCodedRepository repository;
  private List<Anime> animesList;
  @Autowired
  private ResourceLoader resourceLoader;

  @BeforeEach
  void init() {
    var fullMetal = Anime.builder().id(1L).name("Full Metal Alchemist").build();
    var steinsGate = Anime.builder().id(2L).name("Steins Gate").build();
    var mashle = Anime.builder().id(3L).name("Mashle").build();
    animesList = new ArrayList<>(List.of(fullMetal, steinsGate, mashle));
  }

  private String readResourceFile(String filename) throws Exception {
    var file = resourceLoader.getResource("classpath:%s".formatted(filename)).getFile();
    return new String(Files.readAllBytes(file.toPath()));
  }

  @Test
  @DisplayName("GET v1/animes returns a list with all animes when argument name is null")
  @Order(1)
  void findAll_ReturnsAllAnimes_WhenArgumentIsNull() throws Exception {

    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var response = readResourceFile("anime/get-anime-null-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes?name=ufotable returns list with found objects when argument name exists")
  @Order(2)
  void findAll_ReturnsFoundAnimesInList_WhenNameIsFound() throws Exception {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var response = readResourceFile("anime/get-anime-ufotable-name-200.json");
    var name = "Mashle";

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name",
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
    var response = readResourceFile("anime/get-anime-x-name-200.json");
    var name = "x";

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name",
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
    var response = readResourceFile("anime/get-anime-by-id-200.json");
    var id = 1L;

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes/1 throws anime is not found")
  @Order(5)

  void findById_ThrowsResponseStatusException_WhenAnimeNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var id = 99L;

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
  }

  @Test
  @DisplayName("POST v1/animes creates a anime")
  @Order(6)
  void save_CreatesAnime_WhenSucessfull() throws Exception {
    var request = readResourceFile("anime/post-request-anime-200.json");
    var response = readResourceFile("anime/post-response-anime-201.json");
    var animeToSave = Anime.builder().id(99L).name("Overlord").build();
    BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

    mockMvc.perform(MockMvcRequestBuilders
        .post("/v1/animes")
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
    var request = readResourceFile("anime/put-request-anime-200.json");

    mockMvc.perform(MockMvcRequestBuilders
        .put("/v1/animes")
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());

  }

  @Test
  @DisplayName("PUT v1/animes throws ResponseStatusException when anime is not found")
  @Order(8)

  void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
    var request = readResourceFile("anime/put-request-anime-404.json");

    mockMvc.perform(MockMvcRequestBuilders
        .put("/v1/animes")
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));

  }

  @Test
  @DisplayName("DELETE v1/animes/1 removes a anime by Id")
  @Order(9)
  void delete_RemoveAnime_WhenSucessfull() throws Exception {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

    var id = animesList.getFirst().getId();
    mockMvc.perform(MockMvcRequestBuilders
        .delete("/v1/animes/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("DELETE v1/animes/{id} throws ResponseStatusException when anime is not found")
  @Order(10)
  void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

    var id = 99L;
    mockMvc.perform(MockMvcRequestBuilders
        .delete("/v1/animes/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));

  }
}
