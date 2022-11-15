package com.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class CustomerDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String balance;
}
