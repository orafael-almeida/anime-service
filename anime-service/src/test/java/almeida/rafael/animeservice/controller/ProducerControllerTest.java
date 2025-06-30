package almeida.rafael.animeservice.controller;

import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import almeida.rafael.animeservice.domain.Producer;
import almeida.rafael.animeservice.repository.ProducerData;
import almeida.rafael.animeservice.repository.ProducerHardCodedRepository;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "almeida.rafael")
public class ProducerControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private ProducerData producerData;
  @MockitoSpyBean
  private ProducerHardCodedRepository repository;
  private List<Producer> producerList;
  @Autowired
  private ResourceLoader resourceLoader;

  @BeforeEach
  void init() {
    var dateTime = "2025-06-30T09:23:29.150005225";
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nnnnnnnnn");
    var localDateTime = LocalDateTime.parse(dateTime, formatter);

    var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
    var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build();
    var sudioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(localDateTime).build();
    producerList = new ArrayList<>(List.of(ufotable, witStudio, sudioGhibli));
  }

  @Test
  @DisplayName("GET v1/producers returns a list with all producers when argument name is null")
  @Order(1)
  void findAll_ReturnsAllProducer_WhenArgumentIsNull() throws Exception {

    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var response = readResourceFile("producer/get-producer-null-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  private String readResourceFile(String filename) throws Exception {
    var file = resourceLoader.getResource("classpath:%s".formatted(filename)).getFile();
    return new String(Files.readAllBytes(file.toPath()));
  }

  @Test
  @DisplayName("GET v1/producers?name=ufotable returns list with found objects when argument name exists")
  @Order(2)
  void findAll_ReturnsFoundProducersInList_WhenNameIsFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var response = readResourceFile("producer/get-producer-ufotable-name-200.json");
    var name = "Ufotable";

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/producers?name=x returns empty list when name is not found")
  @Order(3)
  void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var response = readResourceFile("producer/get-producer-x-name-200.json");
    var name = "x";

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/producers/1 returns a producer with given id")
  @Order(4)

  void findById_ReturnsProducer_WhenSuccessfull() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var response = readResourceFile("producer/get-producer-by-id-200.json");
    var id = 1L;

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/producers/1 throws ResponseStatusException 404 when producer is not found")
  @Order(5)

  void findById_ThrowsResponseStatusException_WhenProducerNotFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var id = 99L;

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
  }

  @Test
  @DisplayName("POST v1/producers creates a producer")
  @Order(6)

  void save_CreatesProducer_WhenSucessfull() throws Exception {
    var request = readResourceFile("producer/post-request-producer-200.json");
    var response = readResourceFile("producer/post-response-producer-201.json");
    var producerToSave = Producer.builder().id(99L).name("MAPPA").createdAt(LocalDateTime.now()).build();
    BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

    mockMvc.perform(MockMvcRequestBuilders
        .post("/v1/producers")
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }
}
