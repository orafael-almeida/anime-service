package almeida.rafael.animeservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

import almeida.rafael.animeservice.domain.Producer;
import almeida.rafael.animeservice.repository.ProducerHardCodedRepository;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProducerServiceTest {
  @InjectMocks
  private ProducerService service;
  @Mock
  private ProducerHardCodedRepository repository;
  private List<Producer> producerList;

  @BeforeEach
  void init() {
    var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
    var witStudio = Producer.builder().id(2L).name("Wi Studio").createdAt(LocalDateTime.now()).build();
    var sudioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();
    producerList = new ArrayList<>(List.of(ufotable, witStudio, sudioGhibli));
  }

  @Test
  @DisplayName("findAll returns a list with all producers when argument is null")
  @Order(1)
  void findAll_ReturnsAllProducers_WhenArgumentIsNull() {
    BDDMockito.when(repository.findAll()).thenReturn(producerList);

    var producers = service.findAll(null);
    Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producerList);
  }

  @Test
  @DisplayName("findAll returns list with found objects when argument name exists")
  @Order(2)
  void findByName_ReturnsFoundProducersInList_WhenNameIsFound() {
    var producer = producerList.getFirst();
    BDDMockito.when(repository.findByName(producer.getName())).thenReturn(Collections.singletonList(producer));

    var producers = service.findAll(producer.getName());
    Assertions.assertThat(producers).containsAll(Collections.singletonList(producer));
  }

  @Test
  @DisplayName("findAll returns empty list when name is not found")
  @Order(3)
  void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
    var name = "not-found";
    BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

    var producers = service.findAll(name);
    Assertions.assertThat(producers).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("findById returns a producer with given id")
  @Order(4)

  void findById_ReturnsProducers_WhenSuccessfull() {
    var expectedProducer = producerList.getFirst();
    BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));

    var producer = service.findByIdOrThrowNotFound(expectedProducer.getId());

    Assertions.assertThat(producer).isEqualTo(expectedProducer);

  }

  @Test
  @DisplayName("findById throws ResponseStatusException when producer is not found")
  @Order(5)

  void findById_ThrowsResponseStatusException_WhenProducerNotFound() {
    var expectedProducer = producerList.getFirst();
    BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedProducer.getId()))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("save creates a producer")
  @Order(6)

  void save_CreatesProducer_WhenSucessfull() {
    var producerToSave = Producer.builder()
        .id(99L)
        .name("MAPPA")
        .createdAt(LocalDateTime.now())
        .build();
    BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

    var savedProducer = service.save(producerToSave);
    Assertions.assertThat(savedProducer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
  }

  @Test
  @DisplayName("delete removes a producer")
  @Order(7)
  void delete_RemoveProducer_WhenSucessfull() {
    var producerToDelete = producerList.getFirst();

    BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.of(producerToDelete));
    BDDMockito.doNothing().when(repository).delete(producerToDelete);
    Assertions.assertThatNoException().isThrownBy(() -> service.delete(producerToDelete.getId()));

  }

  @Test
  @DisplayName("delete throws ResponseStatusException when producer is not found")
  @Order(8)
  void delete_ThrowsResponseStatusException_WhenProducerIsNotFound() {
    var producerToDelete = producerList.getFirst();

    BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.delete(producerToDelete.getId()))
        .isInstanceOf(ResponseStatusException.class);

  }

  @Test
  @DisplayName("update updates a producer")
  @Order(9)

  void update_UpdatesProducer_WhenSucessfull() {
    var producerToUpdate = producerList.getFirst();
    producerToUpdate.setName("Aniplex");

    BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));
    BDDMockito.doNothing().when(repository).update(producerToUpdate);

    Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToUpdate));

  }

  @Test
  @DisplayName("update throws ResponseStatusException when producer is not found")
  @Order(10)

  void update_ThrowsResponseStatusException_WhenProducerIsNotFound() {
    var producerToUpdate = producerList.getFirst();
    BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.update(producerToUpdate))
        .isInstanceOf(ResponseStatusException.class);

  }
}
