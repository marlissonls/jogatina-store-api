package br.com.jogatinastore.customer.customer.application.mapper;

import br.com.jogatinastore.customer.customer.application.dto.CustomerCreateDto;
import br.com.jogatinastore.customer.customer.application.dto.CustomerResponseDto;
import br.com.jogatinastore.customer.customer.application.dto.CustomerUpdateDto;
import br.com.jogatinastore.customer.customer.domain.model.Customer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { StringUtils.class })
public interface CustomerMapper {
    
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "formatCpf")
    CustomerResponseDto toResponse(Customer customer);

    List<CustomerResponseDto> toResponseList(List<Customer> customers);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "cleanCpf")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "birthDate", source = "birthDate")
    Customer toEntity(CustomerCreateDto dto);

    List<Customer> toEntityList(List<CustomerCreateDto> dtos);


    @BeanMapping(
        ignoreByDefault = true,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "name", source = "name")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "phone", source = "phone")
    void updateEntity(CustomerUpdateDto dto, @MappingTarget Customer customer);
}