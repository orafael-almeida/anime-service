package almeida.rafael.anime_service.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import almeida.rafael.anime_service.domain.Producer;
import almeida.rafael.anime_service.mapper.ProducerMapper;
import almeida.rafael.anime_service.request.ProducerPostRequest;
import almeida.rafael.anime_service.response.ProducerGetResponse;

@RestController
@RequestMapping("v1/producers")
public class ProducerController {

  private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

  @GetMapping
  public List<Producer> listAll(@RequestParam(required = false) String name) {
    var producers = Producer.getProducers();
    if (name == null)
      return producers;

    return producers.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
  }

  @GetMapping("{id}")
  public Producer findById(@PathVariable Long id) {
    return Producer.getProducers().stream().filter(producer -> producer.getId().equals(id)).findFirst().orElse(null);
  }

  @PostMapping()
  public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producePostRequest) {
    var producer = MAPPER.toProducer(producePostRequest);
    var response = MAPPER.toProducerGetResponse(producer);

    Producer.getProducers().add(producer);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }
}
