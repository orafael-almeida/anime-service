package almeida.rafael.animeservice.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import almeida.rafael.animeservice.domain.Producer;
import almeida.rafael.animeservice.request.ProducerPostRequest;
import almeida.rafael.animeservice.request.ProducerPutRequest;
import almeida.rafael.animeservice.response.ProducerGetResponse;
import almeida.rafael.animeservice.response.ProducerPostResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {

  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
  Producer toProducer(ProducerPostRequest postRequest);

  Producer toProducer(ProducerPutRequest request);

  ProducerGetResponse toProducerGetResponse(Producer producer);

  ProducerPostResponse tProducerPostResponse(Producer producer);

  List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producers);

}
