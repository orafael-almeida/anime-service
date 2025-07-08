package almeida.rafael.animeservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import almeida.rafael.animeservice.domain.Producer;
import almeida.rafael.animeservice.exception.NotFoundException;
import almeida.rafael.animeservice.repository.ProducerHardCodedRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProducerService {
  private final ProducerHardCodedRepository repository;

  public List<Producer> findAll(String name) {
    return name == null ? repository.findAll() : repository.findByName(name);
  }

  public Producer findByIdOrThrowNotFound(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Producer not found"));
  }

  public Producer save(Producer producer) {
    return repository.save(producer);
  }

  public void delete(Long id) {
    var producer = findByIdOrThrowNotFound(id);
    repository.delete(producer);
  }

  public void update(Producer producerToUpdate) {
    var producer = findByIdOrThrowNotFound((producerToUpdate.getId()));
    producerToUpdate.setCreatedAt(producer.getCreatedAt());
    repository.update(producerToUpdate);
  }
}
