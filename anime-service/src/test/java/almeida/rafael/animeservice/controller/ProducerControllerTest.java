package almeida.rafael.animeservice.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import almeida.rafael.animeservice.domain.Producer;
import almeida.rafael.animeservice.repository.ProducerData;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "almeida.rafael")
public class ProducerControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private ProducerData producerData;
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
  @DisplayName("findAll returns a list with all producers when argument name is null")
  void findAll_ReturnsAllProducer_WhenArgumentIsNull() throws Exception {

    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var response = readResourceFile("producer/get-producer-null-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  private String readResourceFile(String filename) throws IOException {
    var file = resourceLoader.getResource("classpath:%s".formatted(filename)).getFile();
    return new String(Files.readAllBytes(file.toPath()));
  }

}
