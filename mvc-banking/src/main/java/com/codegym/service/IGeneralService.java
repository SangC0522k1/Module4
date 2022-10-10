package com.codegym.service;


import com.codegym.model.Customer;

import java.util.Optional;

public interface IGeneralService<T> {
    Iterable<T> findAll();

    T getById(Long id);

    Optional<T> findById(Long id);

    T save(T t);

    Iterable<Customer> remove(Long id);
}

