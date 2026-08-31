package br.com.jogatinastore.iam.user.application.mapper;

import br.com.jogatinastore.iam.user.application.dto.UserEmployeeCreateDto;
import br.com.jogatinastore.iam.user.application.dto.UserResponseDto;
import br.com.jogatinastore.iam.user.domain.model.User;
import br.com.jogatinastore.iam.user.application.dto.UserCreateDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { RoleMapperUtils.class })
public interface UserMapper {

    @Mapping(target = "roles", source = "userRoles")
    UserResponseDto toResponse(User user);

    List<UserResponseDto> toResponseList(List<User> users);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    User toEntity(UserCreateDto dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    User toEmployeeEntity(UserEmployeeCreateDto dto);

    List<User> toEntityList(List<UserCreateDto> dtos);
}