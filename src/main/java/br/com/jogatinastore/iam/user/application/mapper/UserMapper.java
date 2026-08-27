package br.com.jogatinastore.iam.user.application.mapper;

import br.com.jogatinastore.iam.user.application.dto.request.CreateEmployeeInput;
import br.com.jogatinastore.iam.user.domain.model.User;
import br.com.jogatinastore.iam.user.application.dto.request.CreateUserInput;
import br.com.jogatinastore.iam.user.application.dto.response.UserResponseOutput;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { RoleMapperUtils.class })
public interface UserMapper {

    @Mapping(target = "roles", source = "userRoles")
    UserResponseOutput toResponse(User user);

    List<UserResponseOutput> toResponseList(List<User> users);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    User toEntity(CreateUserInput dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    User toEmployeeEntity(CreateEmployeeInput dto);

    List<User> toEntityList(List<CreateUserInput> dtos);
}