package com.yuri.hexagonal.adapters.in.controller.mapper;

import com.yuri.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.yuri.hexagonal.adapters.out.client.response.CustomerResponse;
import com.yuri.hexagonal.application.core.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "isValidCpf", ignore = true)
    Customer toCustomer(CustomerRequest customerRequest);

    @Mapping(target = "id", ignore = true)
    CustomerResponse toCustomerResponse(Customer customer);
}
