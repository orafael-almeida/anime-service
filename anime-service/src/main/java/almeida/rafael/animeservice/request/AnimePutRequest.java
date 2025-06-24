package almeida.rafael.animeservice.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AnimePutRequest {
  private Long id;
  private String name;
}
