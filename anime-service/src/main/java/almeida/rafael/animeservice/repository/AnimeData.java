package almeida.rafael.animeservice.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import almeida.rafael.animeservice.domain.Anime;

@Component
public class AnimeData {
  private final List<Anime> animes = new ArrayList<>();

  {
    var naruto = Anime.builder().id(1L).name("Naruto").build();
    var dragonBall = Anime.builder().id(2L).name("Dragon Ball").build();
    var fullMetal = Anime.builder().id(3L).name("Full Metal Alchemist").build();
    animes.addAll(List.of(naruto, dragonBall, fullMetal));
  }

  public List<Anime> getAnimes() {
    return animes;
  }
}
