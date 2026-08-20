package br.com.jogatinastore.domain.customer.customer.mapper;

import br.com.jogatinastore.domain.customer.customer.dto.CustomerCreateDTO;
import br.com.jogatinastore.domain.customer.customer.dto.CustomerResponseDTO;
import br.com.jogatinastore.domain.customer.customer.dto.CustomerUpdateDTO;
import br.com.jogatinastore.domain.customer.customer.entity.Customer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { StringUtils.class })
public interface CustomerMapper {
    
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "formatCpf")
    CustomerResponseDTO toResponse(Customer customer);

    List<CustomerResponseDTO> toResponseList(List<Customer> customers);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "cleanCpf")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "birthDate", source = "birthDate")
    Customer toEntity(CustomerCreateDTO dto);

    List<Customer> toEntityList(List<CustomerCreateDTO> dtos);


    @BeanMapping(
        ignoreByDefault = true,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "name", source = "name")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "phone", source = "phone")
    void updateEntity(CustomerUpdateDTO dto, @MappingTarget Customer customer);
}