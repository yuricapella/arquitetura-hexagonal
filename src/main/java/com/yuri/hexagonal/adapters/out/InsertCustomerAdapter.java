package com.yuri.hexagonal.adapters.out;

import com.yuri.hexagonal.adapters.out.repository.CustomerRepository;
import com.yuri.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.yuri.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.yuri.hexagonal.application.core.domain.Customer;
import com.yuri.hexagonal.application.ports.out.InsertCustomerOutputPort;
import org.springframework.stereotype.Component;

@Component
public class InsertCustomerAdapter implements InsertCustomerOutputPort {

     private final CustomerRepository customerRepository;
     private final CustomerEntityMapper customerEntityMapper;

    public InsertCustomerAdapter(CustomerRepository customerRepository, CustomerEntityMapper customerEntityMapper) {
        this.customerRepository = customerRepository;
        this.customerEntityMapper = customerEntityMapper;
    }

    @Override
    public void insert(Customer customer) {
        CustomerEntity customerEntity = customerEntityMapper.toCustomerEntity(customer);
        customerRepository.save(customerEntity);
    }
}
