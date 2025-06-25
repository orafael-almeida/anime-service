package almeida.rafael.animeservice.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import almeida.rafael.animeservice.domain.Producer;

@Component
public class ProducerData {
  private final List<Producer> producers = new ArrayList<>();

  {
    var mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
    var kyoto = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
    var madHouse = Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build();
    producers.addAll(List.of(mappa, kyoto, madHouse));
  }

  public List<Producer> getProducers() {
    return producers;
  }
}
