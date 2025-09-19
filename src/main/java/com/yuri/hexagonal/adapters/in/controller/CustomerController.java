package com.yuri.hexagonal.adapters.in.controller;

import com.yuri.hexagonal.adapters.in.controller.mapper.CustomerRequestMapper;
import com.yuri.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.yuri.hexagonal.application.core.domain.Customer;
import com.yuri.hexagonal.application.ports.in.InsertCustomerInputPort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final InsertCustomerInputPort insertCustomerInputPort;
    private final CustomerRequestMapper customerRequestMapper;

    public CustomerController(InsertCustomerInputPort insertCustomerInputPort, CustomerRequestMapper customerRequestMapper) {
        this.insertCustomerInputPort = insertCustomerInputPort;
        this.customerRequestMapper = customerRequestMapper;
    }


    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CustomerRequest customerRequest){
        Customer customer = customerRequestMapper.toCustomer(customerRequest);
        insertCustomerInputPort.insert(customer, customerRequest.getZipCode());
        return ResponseEntity.ok().build();
    }
}
