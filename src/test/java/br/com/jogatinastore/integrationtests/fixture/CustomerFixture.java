package br.com.jogatinastore.integrationtests.fixture;

import br.com.jogatinastore.customer.customer.domain.model.Customer;
import br.com.jogatinastore.customer.customer.infrastructure.persistence.CustomerRepository;
import br.com.jogatinastore.iam.user.domain.model.User;
import br.com.jogatinastore.iam.user.infrastructure.persistence.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class CustomerFixture {

    private final CustomerRepository repository;
    private final UserRepository userRepository;

    private String name = "João da Silva";
    private String cpf = "12345678909";
    private LocalDate birthDate = LocalDate.of(1990, 1, 1);
    private String phone = "81999999999";

    public CustomerFixture(
            CustomerRepository repository,
            UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public CustomerFixture withName(String name) {
        this.name = name;
        return this;
    }

    public CustomerFixture withCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public CustomerFixture withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public CustomerFixture withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @Transactional
    public Customer create(User user) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow();

        Customer customer = new Customer();

        customer.assignUser(managedUser);

        customer.setName(name);
        customer.setCpf(cpf);
        customer.setBirthDate(birthDate);
        customer.setPhone(phone);

        return repository.save(customer);
    }
}
