package almeida.rafael.animeservice.controller;

import org.springframework.web.bind.annotation.RestController;

import almeida.rafael.animeservice.domain.Anime;
import almeida.rafael.animeservice.mapper.AnimeMapper;
import almeida.rafael.animeservice.request.AnimePostRequest;
import almeida.rafael.animeservice.response.AnimeGetResponse;
import almeida.rafael.animeservice.response.AnimePostResponse;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
  private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

  @GetMapping
  public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
    log.debug("Request received to list all animes, param name '{}'", name);

    var animes = Anime.getAnimes();
    var animeGetResponseList = MAPPER.toAnimeGetResponseList(animes);
    if (name == null)
      return ResponseEntity.ok(animeGetResponseList);

    var response = animeGetResponseList.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();

    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find anime by id: {}", id);

    var animeGetResponse = Anime.getAnimes()
        .stream()
        .filter(anime -> anime.getId().equals(id))
        .findFirst()
        .map(MAPPER::toAnimeGetResponse)
        .orElse(null);

    return ResponseEntity.ok(animeGetResponse);
  }

  @PostMapping
  public ResponseEntity<AnimePostResponse> Save(@RequestBody AnimePostRequest request) {
    log.debug("Request to find anime by id: {}", request);
    var anime = MAPPER.toAnime(request);

    Anime.getAnimes().add(anime);

    var response = MAPPER.toAnimePostResponse(anime);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }
}
