package almeida.rafael.animeservice.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import almeida.rafael.animeservice.domain.Anime;
import almeida.rafael.animeservice.repository.AnimeHardCodedRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimeService {
  private final AnimeHardCodedRepository repository;

  public List<Anime> findAll(String name) {
    return name == null ? repository.findAll() : repository.findByName(name);
  }

  public Anime findByIdOrThrowNotFound(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
  }

  public Anime save(Anime anime) {
    return repository.save(anime);
  }

  public void delete(Long id) {
    var anime = findByIdOrThrowNotFound(id);
    repository.delete(anime);
  }

  public void update(Anime animeToUpdate) {
    assertAnimeExists(animeToUpdate.getId());
    repository.update(animeToUpdate);
  }

  public void assertAnimeExists(Long id) {
    findByIdOrThrowNotFound(id);
  }
}
