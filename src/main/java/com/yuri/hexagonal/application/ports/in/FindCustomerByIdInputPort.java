package com.yuri.hexagonal.application.ports.in;

import com.yuri.hexagonal.application.core.domain.Customer;

public interface FindCustomerByIdInputPort {
    Customer find(String id);
}
