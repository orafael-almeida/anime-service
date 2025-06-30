package almeida.rafael.animeservice.commons;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import almeida.rafael.animeservice.domain.Anime;

@Component
public class AnimeUtils {
  public List<Anime> newAnimeList() {

    var fullMetal = Anime.builder().id(1L).name("Full Metal Alchemist").build();
    var steinsGate = Anime.builder().id(2L).name("Steins Gate").build();
    var mashle = Anime.builder().id(3L).name("Mashle").build();

    return new ArrayList<>(List.of(fullMetal, steinsGate, mashle));
  }

  public Anime newAnimeToSave() {
    return Anime.builder().id(99L).name("Overlord").build();
  }
}
