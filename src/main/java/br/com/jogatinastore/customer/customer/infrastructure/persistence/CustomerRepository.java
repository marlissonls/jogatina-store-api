package br.com.jogatinastore.customer.customer.infrastructure.persistence;

import br.com.jogatinastore.customer.customer.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByCpf(String cpf);
}
