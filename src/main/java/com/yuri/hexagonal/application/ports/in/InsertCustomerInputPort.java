package com.yuri.hexagonal.application.ports.in;

import com.yuri.hexagonal.application.core.domain.Customer;

public interface InsertCustomerInputPort {
    void insert(Customer customer, String zipCode);
}
