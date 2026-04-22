package br.com.jogatinastore.model.user.mapper;

import br.com.jogatinastore.model.user.User;
import br.com.jogatinastore.model.user.dto.CreateUserDTO;
import br.com.jogatinastore.model.user.dto.UpdateUserDTO;
import br.com.jogatinastore.model.user.dto.UserResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = PermissionMapperUtils.class)
public interface UserMapper {

    @Mapping(target = "permissions", source = "userPermissions")
    UserResponseDTO toResponse(User user);

    List<UserResponseDTO> toResponseList(List<User> users);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "cpf", source = "cpf")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "email", source = "email")
    User toEntity(CreateUserDTO dto);

    List<User> toEntityList(List<CreateUserDTO> dtos);


    @BeanMapping(
        ignoreByDefault = true,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "name", source = "name")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    void updateEntity(UpdateUserDTO dto, @MappingTarget User user);
}