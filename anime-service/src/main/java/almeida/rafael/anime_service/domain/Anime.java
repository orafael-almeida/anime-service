package almeida.rafael.anime_service.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Anime {
  private Long id;
  private String name;
  private static List<Producer> animes = new ArrayList<>();

  static {
    var naruto = new Producer(1L, "Naruto", LocalDateTime.now());
    var dragonBall = new Producer(2L, "Dragon Ball", LocalDateTime.now());
    var fullMetal = new Producer(3L, "Full Metal Alchemist", LocalDateTime.now());
    animes.addAll(List.of(naruto, dragonBall, fullMetal));
  }

  public static List<Producer> getAnimes() {
    return animes;
  }

}
