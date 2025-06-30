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

import almeida.rafael.animeservice.commons.FileUtils;
import almeida.rafael.animeservice.commons.ProducerUtils;
import almeida.rafael.animeservice.domain.Producer;
import almeida.rafael.animeservice.repository.ProducerData;
import almeida.rafael.animeservice.repository.ProducerHardCodedRepository;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "almeida.rafael")
public class ProducerControllerTest {
  private static final String URL = "/v1/producers";
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private ProducerData producerData;
  @MockitoSpyBean
  private ProducerHardCodedRepository repository;
  private List<Producer> producerList;
  @Autowired
  private FileUtils fileUtils;
  @Autowired
  private ProducerUtils producerUtils;

  @BeforeEach
  void init() {
    producerList = producerUtils.newProducerList();
  }

  @Test
  @DisplayName("GET v1/producers returns a list with all producers when argument name is null")
  @Order(1)
  void findAll_ReturnsAllProducer_WhenArgumentIsNull() throws Exception {

    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/producers?name=ufotable returns list with found objects when argument name exists")
  @Order(2)
  void findAll_ReturnsFoundProducersInList_WhenNameIsFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var response = fileUtils.readResourceFile("producer/get-producer-ufotable-name-200.json");
    var name = "Ufotable";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/producers?name=x returns empty list when name is not found")
  @Order(3)
  void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var response = fileUtils.readResourceFile("producer/get-producer-x-name-200.json");
    var name = "x";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/producers/1 returns a producer with given id")
  @Order(4)

  void findById_ReturnsProducer_WhenSuccessfull() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var response = fileUtils.readResourceFile("producer/get-producer-by-id-200.json");
    var id = 1L;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
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

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
  }

  @Test
  @DisplayName("POST v1/producers creates a producer")
  @Order(6)
  void save_CreatesProducer_WhenSucessfull() throws Exception {
    var request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
    var response = fileUtils.readResourceFile("producer/post-response-producer-201.json");
    var producerToSave = producerUtils.newProducerToSave();
    BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

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
  @DisplayName("PUT v1/producers updates a producer")
  @Order(7)
  void update_UpdatesProducer_WhenSucessfull() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var request = fileUtils.readResourceFile("producer/put-request-producer-200.json");

    mockMvc.perform(MockMvcRequestBuilders
        .put(URL)
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());

  }

  @Test
  @DisplayName("PUT v1/producers throws ResponseStatusException when producer is not found")
  @Order(8)

  void update_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var request = fileUtils.readResourceFile("producer/put-request-producer-404.json");

    mockMvc.perform(MockMvcRequestBuilders
        .put(URL)
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))

        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

  }

  @Test
  @DisplayName("DELETE v1/producers/1 removes a producer by Id")
  @Order(9)
  void delete_RemoveProducer_WhenSucessfull() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var id = producerList.getFirst().getId();
    mockMvc.perform(MockMvcRequestBuilders
        .delete(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("DELETE v1/producers/{id} throws ResponseStatusException when producer is not found")
  @Order(10)
  void delete_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var id = 99L;
    mockMvc.perform(MockMvcRequestBuilders
        .delete(URL + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

  }
}
