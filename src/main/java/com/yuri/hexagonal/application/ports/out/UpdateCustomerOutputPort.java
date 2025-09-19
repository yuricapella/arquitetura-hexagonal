package com.yuri.hexagonal.application.ports.out;

import com.yuri.hexagonal.application.core.domain.Customer;

public interface UpdateCustomerOutputPort {
    void update(Customer customer);
}
