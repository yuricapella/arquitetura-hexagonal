package com.yuri.hexagonal.application.ports.out;

import com.yuri.hexagonal.application.core.domain.Address;

public interface FindAddressByZipCodeOutputPort {
    Address find(String zipCode);
}
