package com.yuri.hexagonal.adapters.in.consumer.mapper;

import com.yuri.hexagonal.adapters.in.consumer.message.CustomerMessage;
import com.yuri.hexagonal.application.core.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMessageMapper {

    @Mapping(target = "address", ignore = true)
    Customer toCustomer(CustomerMessage customerMessage);
}
