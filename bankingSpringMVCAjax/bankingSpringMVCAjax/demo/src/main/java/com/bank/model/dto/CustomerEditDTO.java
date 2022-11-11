package com.bank.model.dto;

import com.bank.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CustomerEditDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String balance;

    public Customer toCustomer(){
        return new Customer()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setAddress(address)
                .setBalance(BigDecimal.valueOf(Long.parseLong(balance)));
    }

}
