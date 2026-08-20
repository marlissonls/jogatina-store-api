package br.com.jogatinastore.domain.customer.customer.service;

import br.com.jogatinastore.domain.customer.customer.dto.CustomerCreateDTO;
import br.com.jogatinastore.domain.customer.customer.dto.CustomerUpdateDTO;
import br.com.jogatinastore.domain.customer.customer.dto.CustomerResponseDTO;
import br.com.jogatinastore.domain.customer.customer.entity.Customer;
import br.com.jogatinastore.domain.customer.customer.mapper.CustomerMapper;
import br.com.jogatinastore.domain.customer.customer.repository.CustomerRepository;
import br.com.jogatinastore.domain.customer.customer.exception.CustomerErrors;
import br.com.jogatinastore.domain.iam.user.entity.User;
import br.com.jogatinastore.domain.iam.user.service.UserService;
import br.com.jogatinastore.infra.exception.ConflictException;
import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
import br.com.jogatinastore.shared.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository repository;

    private final CustomerMapper mapper;

    private final UserService userService;

    public CustomerService(
            CustomerRepository repository,
            CustomerMapper mapper,
            UserService userService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.userService = userService;
    }

    public PageResponse<CustomerResponseDTO> findAll(Pageable pageable) {

        logger.debug("Fetching all Customers. page={}", pageable);

        Page<Customer> page = repository.findAll(pageable);

        List<CustomerResponseDTO> items = page.stream()
                .map(mapper::toResponse)
                .toList();

        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    public CustomerResponseDTO findById(UUID id) {

        logger.debug("Starting findById. customerId={}", id);

        Customer customer = findEntityById(id);
        CustomerResponseDTO response = mapper.toResponse(customer);

        logger.info("Customer retrieved successfully. customerId={}", id);

        return response;
    }

    public CustomerResponseDTO me(UUID userId) {

        logger.debug("Fetching authenticated customer profile. customerId={}", userId);

        Customer customer = findEntityById(userId);

        CustomerResponseDTO response = mapper.toResponse(customer);

        logger.info("Customer profile retrieved successfully. customerId={}", userId);

        return response;
    }

    @Transactional
    public CustomerResponseDTO create(UUID userId, CustomerCreateDTO dto) {

        logger.debug("Creating customer. userId={}", userId);

        validateCustomerUniqueness(userId, dto.cpf());

        User user = userService.getValidReference(userId);

        Customer customer = mapper.toEntity(dto);
        customer.assignUser(user);

        Customer savedCustomer = repository.save(customer);

        logger.info("Customer created successfully. customerId={}", savedCustomer.getId());

        return mapper.toResponse(savedCustomer);
    }

    @Transactional
    public CustomerResponseDTO update(UUID id, CustomerUpdateDTO dto) {

        logger.debug("Updating customer. customerId={}", id);

        Customer customer = findEntityById(id);
        applyUpdate(dto, customer);
        Customer updatedCustomer = repository.save(customer);

        logger.info("Customer updated successfully. customerId={}", customer.getId());

        return mapper.toResponse(updatedCustomer);
    }

    private void applyUpdate(CustomerUpdateDTO dto, Customer customer) {
        customer.setName(dto.name());
        customer.setPhone(dto.phone());
        customer.setBirthDate(dto.birthDate());
    }

    @Transactional
    public void delete(UUID id) {

        logger.debug("Deleting customer. customerId={}", id);

        Customer customer = findEntityById(id);

        logger.info("Customer deleted successfully. customerId={}", id);

        repository.delete(customer);
    }

    private Customer findEntityById(UUID id) {

        logger.debug("Fetching customer. customerId={}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Customer not found. customerId={}", id);
                    return new ResourceNotFoundException(
                            CustomerErrors.Target.ID,
                            CustomerErrors.Code.CUSTOMER_NOT_FOUND);
                });
    }

    private void validateCustomerUniqueness(UUID userId, String cpf) {

        if (repository.existsById(userId)) {
            logger.warn("Customer already exists. userId={}", userId);

            throw new ConflictException(
                    CustomerErrors.Target.CUSTOMER,
                    CustomerErrors.Code.CUSTOMER_ALREADY_EXISTS
            );
        }

        if (repository.existsByCpf(cpf)) {
            logger.warn("Cpf already registered. Cpf={}", cpf);

            throw new ConflictException(
                    CustomerErrors.Target.CUSTOMER,
                    CustomerErrors.Code.CUSTOMER_CPF_ALREADY_EXISTS
            );
        }
    }
}
