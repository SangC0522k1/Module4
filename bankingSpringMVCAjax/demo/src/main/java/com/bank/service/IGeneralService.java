package com.bank.service;

import java.util.Optional;

public interface IGeneralService<T> {
    Iterable<T>findfAll();
    T getById(Long id);
    Optional<T> findById(Long id);
    T save(T t);
    void remove(Long id);

}
