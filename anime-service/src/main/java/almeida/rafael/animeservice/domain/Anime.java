package almeida.rafael.animeservice.domain;

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
  private LocalDateTime createdAt;

  private static List<Anime> animes = new ArrayList<>();

  static {
    var naruto = new Anime(1L, "Naruto", LocalDateTime.now());
    var dragonBall = new Anime(2L, "Dragon Ball", LocalDateTime.now());
    var fullMetal = new Anime(3L, "Full Metal Alchemist", LocalDateTime.now());
    animes.addAll(List.of(naruto, dragonBall, fullMetal));
  }

  public static List<Anime> getAnimes() {
    return animes;
  }

}
