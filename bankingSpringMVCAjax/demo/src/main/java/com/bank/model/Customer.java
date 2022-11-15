package com.bank.model;

import com.bank.model.dto.CustomerCreateDTO;
import com.bank.model.dto.CustomerDTO;
import com.bank.model.dto.CustomerEditDTO;
import com.bank.model.dto.RecipientDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
@Accessors(chain = true)
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    private String email;
    private String phone;
    private String address;

    @Column(precision = 12, scale = 0, nullable = false, updatable = false)
    private BigDecimal balance;
    //Lien ket khoa ngoai

    @OneToMany(targetEntity = Deposit.class,  fetch = FetchType.EAGER)
    private Set<Deposit> deposits;

    @OneToMany(targetEntity = Withdraw.class, fetch = FetchType.EAGER)
    private Set<Withdraw> withdraws;

    @OneToMany(targetEntity = Transfer.class, fetch = FetchType.EAGER)
    private Set<Transfer> sender;

    @OneToMany(targetEntity = Transfer.class, fetch = FetchType.EAGER)
    private Set<Transfer> recipient;

    public CustomerDTO toCustomerDTO(){
        return new CustomerDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setAddress(address)
                .setBalance(balance.toString());
    }
    public CustomerCreateDTO toCustomerCreateDTO(){
        return new CustomerCreateDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setAddress(address)
                .setBalance(balance.toString());
    }
    public CustomerEditDTO toCustomerEditDTO(){
        return new CustomerEditDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setAddress(address)
                .setBalance(balance.toString());
    }

}
