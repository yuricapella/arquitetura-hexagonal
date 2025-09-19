package com.yuri.hexagonal.config;

import com.yuri.hexagonal.adapters.out.FindAddressByZipCodeAdapter;
import com.yuri.hexagonal.adapters.out.UpdateCustomerAdapter;
import com.yuri.hexagonal.application.core.usecase.FindCustomerByIdUseCase;
import com.yuri.hexagonal.application.core.usecase.UpdateCustomerUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateCustomerConfig {

    @Bean
    public UpdateCustomerUseCase updateCustomerUseCase(
            FindCustomerByIdUseCase findCustomerByIdUseCase,
            FindAddressByZipCodeAdapter findAddressByZipCodeAdapter,
            UpdateCustomerAdapter updateCustomerAdapter
    ){
        return new UpdateCustomerUseCase(findCustomerByIdUseCase,findAddressByZipCodeAdapter,updateCustomerAdapter);
    }
}
