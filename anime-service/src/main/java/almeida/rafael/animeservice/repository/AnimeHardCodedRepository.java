package almeida.rafael.animeservice.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import almeida.rafael.animeservice.domain.Anime;

public class AnimeHardCodedRepository {
  private static final List<Anime> ANIMES = new ArrayList<>();

  static {
    var naruto = Anime.builder().id(1L).name("Naruto").build();
    var dragonBall = Anime.builder().id(2L).name("Dragon Ball").build();
    var fullMetal = Anime.builder().id(3L).name("Full Metal Alchemist").build();
    ANIMES.addAll(List.of(naruto, dragonBall, fullMetal));
  }

  public List<Anime> findAll() {
    return ANIMES;
  }

  public Optional<Anime> findById(Long id) {
    return ANIMES.stream().filter(anime -> anime.getId().equals(id)).findFirst();
  }

  public List<Anime> findByName(String name) {
    return ANIMES.stream().filter(anime -> anime.getName().equalsIgnoreCase(name))
        .toList();
  }

  public Anime save(Anime anime) {
    ANIMES.add(anime);
    return anime;
  }

  public void delete(Anime anime) {
    ANIMES.remove(anime);
  }

  public void update(Anime anime) {
    delete(anime);
    save(anime);
  }
}
