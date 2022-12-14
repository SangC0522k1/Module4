package com.cg.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferDTO implements Validator {

    private long id;

//    @NotNull(message = "Thông tin người gửi là bắt buộc")
    @Min(value = 1, message = "Thông tin người gửi là bắt buộc")
    private long senderId;

//    @NotNull(message = "Thông tin người nhận là bắt buộc")
    @Min(value = 1, message = "Thông tin người nhận là bắt buộc")
    private long recipientId;


    private String transferAmount;

    @Override
    public boolean supports(Class<?> aClass) {
        return TransferDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransferDTO transferDTO = (TransferDTO) target;

        String transferAmount = transferDTO.getTransferAmount();

        if (transferAmount != null && transferAmount.length() > 0) {
            if (transferAmount.length() > 9){
                errors.rejectValue("transferAmount", "transferAmount.max", "Số tiền chuyển khoản tối đa là 999.999.999");
                return;
            }

            if (!transferAmount.matches("(^$|[0-9]*$)")){
                errors.rejectValue("transferAmount", "transferAmount.number", "Chỉ chấp nhận số tiền chuyển khoản là ký tự số");
                return;
            }

            float transactionAmountFloat= Float.parseFloat(transferAmount);

            if (transactionAmountFloat % 10 > 0) {
                errors.rejectValue("transferAmount", "transferAmount.decimal", "Số tiền chuyển khoản phải là số chẵn chia hết cho 10");
            }

        } else {
            errors.rejectValue("transferAmount",  "transferAmount.null", "Số tiền chuyển khoản là bắt buộc");
        }
    }
}
