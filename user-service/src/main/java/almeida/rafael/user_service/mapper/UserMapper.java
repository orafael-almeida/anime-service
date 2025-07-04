package almeida.rafael.user_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import almeida.rafael.user_service.domain.User;
import almeida.rafael.user_service.request.UserPostRequest;
import almeida.rafael.user_service.request.UserPutRequest;
import almeida.rafael.user_service.response.UserGetResponse;
import almeida.rafael.user_service.response.UserPostResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
  @org.mapstruct.Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
  User toUser(UserPostRequest postRequest);

  User toUser(UserPutRequest user);

  UserPostResponse toUserPostResponse(User user);

  UserGetResponse toUserGetResponse(User user);

  List<UserGetResponse> toUserGetResponseList(List<User> users);
}
