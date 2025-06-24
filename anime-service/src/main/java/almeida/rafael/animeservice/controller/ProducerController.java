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
import almeida.rafael.animeservice.mapper.ProducerMapper;
import almeida.rafael.animeservice.request.ProducerPostRequest;
import almeida.rafael.animeservice.request.ProducerPutRequest;
import almeida.rafael.animeservice.response.ProducerGetResponse;
import almeida.rafael.animeservice.response.ProducerPostResponse;
import almeida.rafael.animeservice.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/producers")
@RequiredArgsConstructor
@Slf4j
public class ProducerController {
  private final ProducerMapper mapper;
  private final ProducerService service;

  @GetMapping
  public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name) {
    log.debug("Request received to list all animes, param name '{}'", name);

    var producers = service.findAll(name);
    var producerGetResponses = mapper.toProducerGetResponseList(producers);

    return ResponseEntity.ok(producerGetResponses);
  }

  @GetMapping("{id}")
  public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find producer by id: {}", id);

    var producer = service.findByIdOrThrowNotFound(id);
    var producerGetResponse = mapper.toProducerGetResponse(producer);
    return ResponseEntity.ok(producerGetResponse);
  }

  @PostMapping()
  public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producePostRequest) {
    log.debug("Request do save a producer: {}", producePostRequest);

    var producer = mapper.toProducer(producePostRequest);
    var producerSaved = service.save(producer);
    var producerGetResponse = mapper.tProducerPostResponse(producerSaved);

    return ResponseEntity.status(HttpStatus.CREATED).body(producerGetResponse);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    log.debug("Request do delete producer by id: {}", id);

    service.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> updateProducer(@RequestBody ProducerPutRequest request) {
    log.debug("Request to update producer {}", request);

    var producerToUpdated = mapper.toProducer(request);
    service.update(producerToUpdated);

    return ResponseEntity.noContent().build();
  }
}
