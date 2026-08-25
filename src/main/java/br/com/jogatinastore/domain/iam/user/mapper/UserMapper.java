package br.com.jogatinastore.domain.iam.user.mapper;

import br.com.jogatinastore.domain.iam.user.dto.CreateEmployeeDTO;
import br.com.jogatinastore.domain.iam.user.entity.User;
import br.com.jogatinastore.domain.iam.user.dto.CreateUserDTO;
import br.com.jogatinastore.domain.iam.user.dto.UserResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { RoleMapperUtils.class })
public interface UserMapper {

    @Mapping(target = "roles", source = "userRoles")
    UserResponseDTO toResponse(User user);

    List<UserResponseDTO> toResponseList(List<User> users);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    User toEntity(CreateUserDTO dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    User toEmployeeEntity(CreateEmployeeDTO dto);

    List<User> toEntityList(List<CreateUserDTO> dtos);
}