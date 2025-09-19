package com.yuri.hexagonal.adapters.in.controller;

import com.yuri.hexagonal.adapters.in.controller.mapper.CustomerMapper;
import com.yuri.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.yuri.hexagonal.adapters.out.client.response.CustomerResponse;
import com.yuri.hexagonal.application.core.domain.Customer;
import com.yuri.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.yuri.hexagonal.application.ports.in.InsertCustomerInputPort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final InsertCustomerInputPort insertCustomerInputPort;
    private final FindCustomerByIdInputPort findCustomerByIdInputPort;
    private final CustomerMapper customerMapper;

    public CustomerController(InsertCustomerInputPort insertCustomerInputPort, FindCustomerByIdInputPort findCustomerByIdInputPort, CustomerMapper customerMapper) {
        this.insertCustomerInputPort = insertCustomerInputPort;
        this.findCustomerByIdInputPort = findCustomerByIdInputPort;
        this.customerMapper = customerMapper;
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CustomerRequest customerRequest){
        Customer customer = customerMapper.toCustomer(customerRequest);
        insertCustomerInputPort.insert(customer, customerRequest.getZipCode());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
     public ResponseEntity<CustomerResponse> findById(@PathVariable final String id){
        Customer customer = findCustomerByIdInputPort.find(id);
        CustomerResponse customerResponse = customerMapper.toCustomerResponse(customer);
        return ResponseEntity.ok().body(customerResponse);
     }
}
