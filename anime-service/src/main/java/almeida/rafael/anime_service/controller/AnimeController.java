package almeida.rafael.anime_service.controller;

import org.springframework.web.bind.annotation.RestController;
import almeida.rafael.anime_service.domain.Producer;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("v1/animes")
public class AnimeController {

  @GetMapping
  public List<Producer> listAll(@RequestParam(required = false) String name) {
    var animes = Producer.getProducers();
    if (name == null)
      return animes;

    return animes.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
  }

  @GetMapping("{id}")
  public Producer findById(@PathVariable Long id) {
    return Producer.getProducers().stream().filter(anime -> anime.getId().equals(id)).findFirst().orElse(null);
  }

  @PostMapping
  public Producer Save(@RequestBody Producer anime) {
    anime.setId(ThreadLocalRandom.current().nextLong(100_000));
    Producer.getProducers().add(anime);
    return anime;

  }
}
