package almeida.rafael.animeservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import almeida.rafael.animeservice.domain.Anime;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnimeHardCodedRepository {
  private final AnimeData animeData;

  public List<Anime> findAll() {
    return animeData.getAnimes();
  }

  public Optional<Anime> findById(Long id) {
    return animeData.getAnimes().stream().filter(anime -> anime.getId().equals(id)).findFirst();
  }

  public List<Anime> findByName(String name) {
    return animeData.getAnimes().stream().filter(anime -> anime.getName().equalsIgnoreCase(name))
        .toList();
  }

  public Anime save(Anime anime) {
    animeData.getAnimes().add(anime);
    return anime;
  }

  public void delete(Anime anime) {
    animeData.getAnimes().remove(anime);
  }

  public void update(Anime anime) {
    delete(anime);
    save(anime);
  }
}
