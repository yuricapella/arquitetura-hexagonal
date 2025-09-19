package com.yuri.hexagonal.adapters.out.client.mapper;

import com.yuri.hexagonal.adapters.out.client.response.AddressResponse;
import com.yuri.hexagonal.application.core.domain.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressResponseMapper {
    Address toAddress(AddressResponse addressResponse);
    AddressResponse toAddressResponse(Address address);
}
