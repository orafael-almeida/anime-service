package almeida.rafael.animeservice.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import almeida.rafael.animeservice.domain.Producer;
import almeida.rafael.animeservice.mapper.ProducerMapper;
import almeida.rafael.animeservice.request.ProducerPostRequest;
import almeida.rafael.animeservice.request.ProducerPutRequest;
import almeida.rafael.animeservice.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {
  private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

  @GetMapping
  public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name) {
    log.debug("Request received to list all animes, param name '{}'", name);

    var producers = Producer.getProducers();
    var producerGetResponseList = MAPPER.toProducerGetResponseList(producers);
    if (name == null)
      return ResponseEntity.ok(producerGetResponseList);

    var response = producerGetResponseList.stream().filter(producer -> producer.getName().equalsIgnoreCase(name))
        .toList();

    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find producer by id: {}", id);

    var producerGetResponse = Producer.getProducers()
        .stream()
        .filter(producer -> producer.getId().equals(id))
        .findFirst()
        .map(MAPPER::toProducerGetResponse)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

    return ResponseEntity.ok(producerGetResponse);
  }

  @PostMapping()
  public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producePostRequest) {
    var producer = MAPPER.toProducer(producePostRequest);
    var response = MAPPER.toProducerGetResponse(producer);

    Producer.getProducers().add(producer);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    log.debug("Request do delete producer by id: {}", id);

    var producerToDelete = Producer.getProducers()
        .stream()
        .filter(producer -> producer.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));
    Producer.getProducers().remove(producerToDelete);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> updateProducer(@RequestBody ProducerPutRequest request) {
    log.debug("Request to update producer {}", request);

    var producerToRemove = Producer.getProducers()
        .stream()
        .filter(producer -> producer.getId().equals(request.getId()))
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

    var producerUpdated = MAPPER.toProducer(request, producerToRemove.getCreatedAt());

    Producer.getProducers().remove(producerToRemove);
    Producer.getProducers().add(producerUpdated);

    return ResponseEntity.noContent().build();
  }
}
