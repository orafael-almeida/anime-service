package almeida.rafael.animeservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import almeida.rafael.animeservice.domain.Producer;
import almeida.rafael.animeservice.request.ProducerPostRequest;
import almeida.rafael.animeservice.response.ProducerGetResponse;

@Mapper
public interface ProducerMapper {
  ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
  Producer toProducer(ProducerPostRequest postRequest);

  ProducerGetResponse toProducerGetResponse(Producer producer);
}
