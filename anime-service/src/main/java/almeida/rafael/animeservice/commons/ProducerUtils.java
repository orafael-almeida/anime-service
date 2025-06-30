package almeida.rafael.animeservice.commons;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import almeida.rafael.animeservice.domain.Producer;

@Component
public class ProducerUtils {
  public List<Producer> newProducerList() {
    var dateTime = "2025-06-30T09:23:29.150005225";
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nnnnnnnnn");
    var localDateTime = LocalDateTime.parse(dateTime, formatter);

    var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
    var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build();
    var sudioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(localDateTime).build();

    return new ArrayList<>(List.of(ufotable, witStudio, sudioGhibli));
  }

  public Producer newProducerToSave() {
    return Producer.builder().id(99L).name("MAPPA").createdAt(LocalDateTime.now()).build();
  }

}
