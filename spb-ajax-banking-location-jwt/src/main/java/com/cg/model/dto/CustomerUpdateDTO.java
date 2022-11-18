package com.cg.model.dto;


import com.cg.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CustomerUpdateDTO {
    private long id;

        @NotEmpty(message = "Vui lòng nhập tên đầy đủ")
    @Size(min = 5, max = 50, message = "Họ tên có độ dài nằm trong khoảng 5-50 ký tự")
    private String fullName;

    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Vui lòng nhập đúng định dạng số điện thoại bắt đầu bằng đầu số 03,05,07,08,09,84")
    private String phone;

    private String balance;

    private LocationRegionDTO locationRegion;

    public Customer toUpdateCustomer(){
        return new Customer()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(new BigDecimal(balance))
                .setLocationRegion(locationRegion.toLocationRegion());
    }
}
