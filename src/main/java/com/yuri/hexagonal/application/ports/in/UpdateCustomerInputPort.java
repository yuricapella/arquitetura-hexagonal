package com.yuri.hexagonal.application.ports.in;

import com.yuri.hexagonal.application.core.domain.Customer;

public interface UpdateCustomerInputPort {
    void update(Customer customer, String zipCode);
}
