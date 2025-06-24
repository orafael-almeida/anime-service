package almeida.rafael.animeservice.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producer {
  @EqualsAndHashCode.Include
  private Long id;
  @JsonProperty("name")
  private String name;
  private LocalDateTime createdAt;
  private static List<Producer> producers = new ArrayList<>();

  static {
    var mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
    var kyoto = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
    var madHouse = Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build();
    producers.addAll(List.of(mappa, kyoto, madHouse));
  }

  public static List<Producer> getProducers() {
    return producers;
  }

}
