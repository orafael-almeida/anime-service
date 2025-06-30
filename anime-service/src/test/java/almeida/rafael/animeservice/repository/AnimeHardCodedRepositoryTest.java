package almeida.rafael.animeservice.repository;

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

import almeida.rafael.animeservice.commons.AnimeUtils;
import almeida.rafael.animeservice.domain.Anime;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnimeHardCodedRepositoryTest {
  @InjectMocks
  private AnimeHardCodedRepository repository;

  @Mock
  private AnimeData animeData;
  private List<Anime> animeList;
  @InjectMocks
  private AnimeUtils animeUtils;

  @BeforeEach
  void init() {

    animeList = animeUtils.newAnimeList();
  }

  @Test
  @DisplayName("findAll returns a list with all animes")
  @Order(1)

  void findAll_ReturnsAllAnimes_WhenSuccessfull() {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
    var animes = repository.findAll();
    Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);

  }

  @Test
  @DisplayName("findById returns a anime with given id")
  @Order(2)

  void findById_ReturnsAnimes_WhenSuccessfull() {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

    var expectedAnime = animeList.getFirst();
    var animes = repository.findById(expectedAnime.getId());

    Assertions.assertThat(animes).isPresent().contains(expectedAnime);

  }

  @Test
  @DisplayName("findByName returns an empty list when name is null")
  @Order(3)

  void findByName_ReturnsEmptyList_WhenNameIsNull() {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

    var animes = repository.findByName(null);
    Assertions.assertThat(animes).isNotNull().isEmpty();

  }

  @Test
  @DisplayName("findByName returns list with found object when name exists")
  @Order(4)

  void findByName_ReturnsFoundAnimeInList_WhenNameIsFound() {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

    var expectedAnime = animeList.getFirst();
    var animes = repository.findByName(expectedAnime.getName());
    Assertions.assertThat(animes).contains(expectedAnime);
  }

  @Test
  @DisplayName("save creates an anime")
  @Order(5)

  void save_CreatesAnime_WhenSucessfull() {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

    var animeToSave = animeUtils.newAnimeToSave();

    var anime = repository.save(animeToSave);
    Assertions.assertThat(anime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();

    var animeSavedOptional = repository.findById(animeToSave.getId());
    Assertions.assertThat(animeSavedOptional).isPresent().contains(animeToSave);
  }

  @Test
  @DisplayName("delete removes an anime")
  @Order(6)
  void delete_RemoveAnime_WhenSucessfull() {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

    var animeToDelete = animeList.getFirst();
    repository.delete(animeToDelete);

    var animes = repository.findAll();
    Assertions.assertThat(animes).isNotEmpty().doesNotContain(animeToDelete);
  }

  @Test
  @DisplayName("update updates an anime")
  @Order(7)

  void update_UpdatesAnime_WhenSucessfull() {
    BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

    var animeToUpdate = this.animeList.getFirst();
    animeToUpdate.setName("Hellsing");

    repository.update(animeToUpdate);

    Assertions.assertThat(this.animeList).contains(animeToUpdate);
    var updatedAnimeOptional = repository.findById(animeToUpdate.getId());
    Assertions.assertThat(updatedAnimeOptional).isPresent();
    Assertions.assertThat(updatedAnimeOptional.get().getName()).isEqualTo(animeToUpdate.getName());
  }
}
