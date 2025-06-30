package almeida.rafael.animeservice.service;

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

import almeida.rafael.animeservice.commons.AnimeUtils;
import almeida.rafael.animeservice.domain.Anime;
import almeida.rafael.animeservice.repository.AnimeHardCodedRepository;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnimeServiceTest {
  @InjectMocks
  private AnimeService service;
  @Mock
  private AnimeHardCodedRepository repository;
  private List<Anime> animesList;

  @InjectMocks
  private AnimeUtils animeUtils;

  @BeforeEach
  void init() {

    animesList = animeUtils.newAnimeList();
  }

  @Test
  @DisplayName("findAll returns a list with all animes when argument is null")
  @Order(1)
  void findAll_ReturnsAllAnimes_WhenArgumentIsNull() {
    BDDMockito.when(repository.findAll()).thenReturn(animesList);

    var animes = service.findAll(null);
    Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animesList);
  }

  @Test
  @DisplayName("findAll returns list with found objects when argument name exists")
  @Order(2)
  void findByName_ReturnsFoundAnimesInList_WhenNameIsFound() {
    var anime = animesList.getFirst();
    BDDMockito.when(repository.findByName(anime.getName())).thenReturn(Collections.singletonList(anime));

    var animesFound = service.findAll(anime.getName());
    Assertions.assertThat(animesFound).containsAll(Collections.singletonList(anime));
  }

  @Test
  @DisplayName("findAll returns empty list when name is not found")
  @Order(3)
  void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
    var name = "not-found";
    BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

    var animes = service.findAll(name);
    Assertions.assertThat(animes).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("findById returns an anime with given id")
  @Order(4)

  void findById_ReturnsAnimes_WhenSuccessfull() {
    var expectedAnime = animesList.getFirst();
    BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));

    var animes = service.findByIdOrThrowNotFound(expectedAnime.getId());

    Assertions.assertThat(animes).isEqualTo(expectedAnime);

  }

  @Test
  @DisplayName("findById throws ResponseStatusException when anime is not found")
  @Order(5)

  void findById_ThrowsResponseStatusException_WhenAnimeNotFound() {
    var expectedAnime = animesList.getFirst();
    BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedAnime.getId()))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("save creates an anime")
  @Order(6)

  void save_CreatesAnime_WhenSucessfull() {
    var animeToSave = animeUtils.newAnimeToSave();
    BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

    var savedAnime = service.save(animeToSave);
    Assertions.assertThat(savedAnime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
  }

  @Test
  @DisplayName("delete removes an anime")
  @Order(7)
  void delete_RemoveAnime_WhenSucessfull() {
    var animeToDelete = animesList.getFirst();

    BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
    BDDMockito.doNothing().when(repository).delete(animeToDelete);
    Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));

  }

  @Test
  @DisplayName("delete throws ResponseStatusException when anime is not found")
  @Order(8)
  void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
    var animeToDelete = animesList.getFirst();

    BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.delete(animeToDelete.getId()))
        .isInstanceOf(ResponseStatusException.class);

  }

  @Test
  @DisplayName("update updates an anime")
  @Order(9)

  void update_UpdatesAnime_WhenSucessfull() {
    var animeToUpdate = animesList.getFirst();
    animeToUpdate.setName("Aniplex");

    BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
    BDDMockito.doNothing().when(repository).update(animeToUpdate);

    Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));

  }

  @Test
  @DisplayName("update throws ResponseStatusException when anime is not found")
  @Order(10)

  void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
    var animeToUpdate = animesList.getFirst();
    BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.update(animeToUpdate))
        .isInstanceOf(ResponseStatusException.class);

  }
}
