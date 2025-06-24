package almeida.rafael.animeservice.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProducerGetResponse {
  private Long id;
  private String name;
  private LocalDateTime createdAt;

}
