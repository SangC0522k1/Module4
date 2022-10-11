package com.codegym.service.customer;


import com.codegym.model.Customer;
import com.codegym.service.IGeneralService;

import java.util.List;

public interface ICustomerService extends IGeneralService<Customer> {
    Iterable<Customer> findAllByDeletedIsFalse();

    List<Customer> findByIdIsNot (Long id);
}
