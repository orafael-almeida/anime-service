package almeida.rafael.animeservice.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import almeida.rafael.animeservice.domain.Producer;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProducerHardCodedRepositoryTest {
  @InjectMocks
  private ProducerHardCodedRepository repository;

  @Mock
  private ProducerData producerData;
  private List<Producer> producerList;

  @BeforeEach
  void init() {
    var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
    var witStudio = Producer.builder().id(2L).name("Wi Studio").createdAt(LocalDateTime.now()).build();
    var sudioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();
    producerList = new ArrayList<>(List.of(ufotable, witStudio, sudioGhibli));
  }

  @Test
  @DisplayName("findAll returns a list with all producers")
  @Order(1)

  void findAll_ReturnsAllProducers_WhenSuccessfull() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var producers = repository.findAll();
    Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producerList);

  }

  @Test
  @DisplayName("findById returns a producer with given id")
  @Order(2)

  void findById_ReturnsProducers_WhenSuccessfull() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var expectedProducer = producerList.getFirst();
    var producers = repository.findById(expectedProducer.getId());

    Assertions.assertThat(producers).isPresent().contains(expectedProducer);

  }

  @Test
  @DisplayName("findByName returns an empty list when name is null")
  @Order(3)

  void findByName_ReturnsEmptyList_WhenNameIsNull() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var producers = repository.findByName(null);
    Assertions.assertThat(producers).isNotNull().isEmpty();

  }

  @Test
  @DisplayName("findByName returns list with found object when name exists")
  @Order(4)

  void findByName_ReturnsFoundProducerInList_WhenNameIsFound() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var expectedProducer = producerList.getFirst();
    var producers = repository.findByName(expectedProducer.getName());
    Assertions.assertThat(producers).contains(expectedProducer);
  }

  @Test
  @DisplayName("save creates a producer")
  @Order(5)

  void save_CreatesProducer_WhenSucessfull() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var producerToSave = Producer.builder()
        .id(99L)
        .name("MAPPA")
        .createdAt(LocalDateTime.now())
        .build();

    var producer = repository.save(producerToSave);
    Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
    var producerSavedOptional = repository.findById(producerToSave.getId());
    Assertions.assertThat(producerSavedOptional).isPresent().contains(producerToSave);
  }

  @Test
  @DisplayName("delete removes a producer")
  @Order(6)
  void delete_RemoveProducer_WhenSucessfull() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var producerToDelete = producerList.getFirst();
    repository.delete(producerToDelete);

    var producers = repository.findAll();
    Assertions.assertThat(producers).isNotEmpty().doesNotContain(producerToDelete);
  }

  @Test
  @DisplayName("update updates a producer")
  @Order(7)

  void update_UpdatesProducer_WhenSucessfull() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var producerToUpdate = this.producerList.getFirst();
    producerToUpdate.setName("Aniplex");

    repository.update(producerToUpdate);

    Assertions.assertThat(this.producerList).contains(producerToUpdate);
    var updatedProducerOptional = repository.findById(producerToUpdate.getId());
    Assertions.assertThat(updatedProducerOptional).isPresent();
    Assertions.assertThat(updatedProducerOptional.get().getName()).isEqualTo(producerToUpdate.getName());
  }
}
