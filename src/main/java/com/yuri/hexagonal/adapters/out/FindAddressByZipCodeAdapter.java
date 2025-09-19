package com.yuri.hexagonal.adapters.out;

import com.yuri.hexagonal.adapters.out.client.FindAddressByZipCodeClient;
import com.yuri.hexagonal.adapters.out.client.mapper.AddressResponseMapper;
import com.yuri.hexagonal.adapters.out.client.response.AddressResponse;
import com.yuri.hexagonal.application.core.domain.Address;
import com.yuri.hexagonal.application.ports.out.FindAddressByZipCodeOutputPort;
import org.springframework.stereotype.Component;

@Component
public class FindAddressByZipCodeAdapter implements FindAddressByZipCodeOutputPort {
    private final FindAddressByZipCodeClient findAddressByZipCodeClient;
    private final AddressResponseMapper addressResponseMapper;

    public FindAddressByZipCodeAdapter(FindAddressByZipCodeClient findAddressByZipCodeClient, AddressResponseMapper addressResponseMapper) {
        this.findAddressByZipCodeClient = findAddressByZipCodeClient;
        this.addressResponseMapper = addressResponseMapper;
    }

    @Override
    public Address find(String zipCode) {
        AddressResponse addressResponse = findAddressByZipCodeClient.find(zipCode);

        return addressResponseMapper.toAddress(addressResponse);
    }
}
