package br.com.jogatinastore.domain.customer.customer.repository;

import br.com.jogatinastore.domain.customer.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByCpf(String cpf);
}
