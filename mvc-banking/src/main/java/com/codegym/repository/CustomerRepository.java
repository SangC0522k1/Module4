package com.codegym.repository;


import com.codegym.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Iterable<Customer> findAllByDeletedIsFalse();

    List<Customer> findByIdIsNot (Long id);
}
