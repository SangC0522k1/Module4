package com.cg.model;

import com.cg.model.dto.CustomerCreateDTO;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.RecipientDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


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
    private long id;

    @Column(name = "full_name")
//    @NotEmpty(message = "Vui lòng nhập tên đầy đủ")
//    @Size(min = 10, max = 50, message = "Họ tên có độ dài nằm trong khoảng 10-50 ký tự")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

//    @Pattern(regexp = "^[0][0-9]{9}$", message = "Vui lòng nhập đúng định dạng số điện thoại")
    private String phone;

    private String address;

    @Column(precision = 12, scale = 0, nullable = false, updatable = false)
    private BigDecimal balance;


    @OneToMany(targetEntity = Deposit.class, fetch = FetchType.EAGER)
    private List<Deposit> deposits;


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                '}';
    }

    public CustomerCreateDTO toCustomerCreateDTO() {
        return new CustomerCreateDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setAddress(address)
                .setBalance(balance.toString());
    }

    public CustomerDTO toCustomerDTO() {
        return new CustomerDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setAddress(address)
                .setBalance(balance.toString());
    }

    public RecipientDTO toRecipientDTO() {
        return new RecipientDTO()
                .setId(id)
                .setFullName(fullName);
    }

}
