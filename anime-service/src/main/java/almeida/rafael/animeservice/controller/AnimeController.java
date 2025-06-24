package almeida.rafael.animeservice.controller;

import org.springframework.web.bind.annotation.RestController;
import almeida.rafael.animeservice.mapper.AnimeMapper;
import almeida.rafael.animeservice.request.AnimePostRequest;
import almeida.rafael.animeservice.request.AnimePutRequest;
import almeida.rafael.animeservice.response.AnimeGetResponse;
import almeida.rafael.animeservice.response.AnimePostResponse;
import almeida.rafael.animeservice.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("v1/animes")
@RequiredArgsConstructor
@Slf4j
public class AnimeController {
  private final AnimeMapper mapper;
  private final AnimeService service;

  @GetMapping
  public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
    log.debug("Request received to list all animes, param name '{}'", name);

    var animes = service.findAll(name);
    var animeGetResponses = mapper.toAnimeGetResponseList(animes);

    return ResponseEntity.ok(animeGetResponses);
  }

  @GetMapping("{id}")
  public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find anime by id: {}", id);

    var anime = service.findByIdOrThrowNotFound(id);
    var animeGetResponse = mapper.toAnimeGetResponse(anime);
    return ResponseEntity.ok(animeGetResponse);
  }

  @PostMapping
  public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest request) {
    log.debug("Request to find anime by id: {}", request);
    var anime = mapper.toAnime(request);
    var animeSaved = service.save(anime);
    var animeGetResponse = mapper.toAnimePostResponse(animeSaved);

    return ResponseEntity.status(HttpStatus.CREATED).body(animeGetResponse);

  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteAnimeById(@PathVariable Long id) {
    log.debug("Request to delete anime by id: {}", id);

    service.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> updateAnime(@RequestBody AnimePutRequest request) {
    log.debug("Request to update anime {}", request);

    var animeToUpdated = mapper.toAnime(request);
    service.update(animeToUpdated);

    return ResponseEntity.noContent().build();
  }
}
