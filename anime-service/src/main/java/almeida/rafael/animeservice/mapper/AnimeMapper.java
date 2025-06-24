package almeida.rafael.animeservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import almeida.rafael.animeservice.domain.Anime;
import almeida.rafael.animeservice.request.AnimePostRequest;
import almeida.rafael.animeservice.request.AnimePutRequest;
import almeida.rafael.animeservice.response.AnimeGetResponse;
import almeida.rafael.animeservice.response.AnimePostResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

  @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
  Anime toAnime(AnimePostRequest postRequest);

  Anime toAnime(AnimePutRequest anime);

  AnimePostResponse toAnimePostResponse(Anime anime);

  AnimeGetResponse toAnimeGetResponse(Anime anime);

  List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animes);

}
